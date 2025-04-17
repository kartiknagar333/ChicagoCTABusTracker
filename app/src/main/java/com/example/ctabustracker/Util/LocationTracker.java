package com.example.ctabustracker.Util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Date;
import java.util.Locale;

public class LocationTracker {

    private static final int LOCATION_REQUEST = 111;
    private final Activity activity;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = false;
    private LatLng lastLL, newLL;
    private double totalDist = 0;
    private int updateCount = 0;
    
    private final LocationUpdateListener listener;

    public interface LocationUpdateListener {
        void onLocationUpdate(String timestamp, String coordinates, String updateInfo);
    }

    public LocationTracker(Activity activity, LocationUpdateListener listener) {
        this.activity = activity;
        this.listener = listener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        setupLocationRequest();
        setupLocationCallback();
    }

    private void setupLocationRequest() {
        LocationRequest.Builder builder = new LocationRequest.Builder(1000);
        builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        builder.setIntervalMillis(1000);
        builder.setMinUpdateIntervalMillis(500);
        builder.setMaxUpdateDelayMillis(1500);
        locationRequest = builder.build();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    lastLL = newLL;
                    newLL = new LatLng(lat, lon);

                    double distance = getDistance();
                    totalDist += distance;
                    double distanceKm = totalDist / 1000.0;
                    String updateInfo = String.format(Locale.getDefault(),
                            "Update Count: #%d%n%n" +
                                    "Total Distance (km): %.2f%n%n" +
                                    "Travel Direction: %.1f° (%s)%n%n" +
                                    "Speed (mph): %.1f",
                            ++updateCount, distanceKm, 
                            location.getBearing(), getDir(location.getBearing()), 
                            location.getSpeed() * 2.23694);
                    
                    listener.onLocationUpdate(new Date().toString(), 
                            String.format(Locale.getDefault(), "Last: %f, %f", lat, lon), 
                            updateInfo);
                }
            }
        };
    }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        if (!checkPermissions()) return;

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        requestingLocationUpdates = true;
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    private double getDistance() {
        if (lastLL == null || newLL == null)
            return 0;
        return SphericalUtil.computeDistanceBetween(lastLL, newLL);
    }

    private String getDir(float degIn) {
        int deg = 45 * (Math.round(degIn / 45));
        switch (deg) {
            case 0: return "N";
            case 45: return "NE";
            case 90: return "E";
            case 135: return "SE";
            case 180: return "S";
            case 225: return "SW";
            case 270: return "W";
            case 315: return "NW";
            default: return "Unknown";
        }
    }


}
