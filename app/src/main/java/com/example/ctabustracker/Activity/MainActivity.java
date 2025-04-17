package com.example.ctabustracker.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ctabustracker.API.GetDirection;
import com.example.ctabustracker.API.Getroutes;
import com.example.ctabustracker.Adapter.RouteItemAdapter;
import com.example.ctabustracker.Ads.AdsCallback;
import com.example.ctabustracker.Ads.BannerViewListener;
import com.example.ctabustracker.Ads.UnityInitializationListener;
import com.example.ctabustracker.Model.Routes;
import com.example.ctabustracker.R;
import com.example.ctabustracker.Util.LocationTracker;
import com.example.ctabustracker.Util.NetworkUtil;
import com.example.ctabustracker.databinding.ActivityMainBinding;
import com.example.ctabustracker.fetcher.GetRoutesfetcher;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationTracker.LocationUpdateListener , AdsCallback {
    private ActivityMainBinding binding;
    private boolean keepOn = true;
    private static final long minSplashTime = 2000;
    private long startTime;
    private Getroutes getroutes;
    private List<Routes> routeslist = new ArrayList<>();
    private List<Routes> filterlist = new ArrayList<>();
    private LocationTracker locationTracker;
    private SharedPreferences spf;
    private SharedPreferences.Editor edit;
    private RouteItemAdapter adapter = null;
    private GetDirection getDirection;
    private double lat,lon;
    private static final String unityGameID = "4921141";
    private static final boolean testMode = true;
    private static final String bannerPlacement = "Banner_Android";
    private BannerView.IListener bannerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        startTime = System.currentTimeMillis();
        SplashScreen.installSplashScreen(this)
                .setKeepOnScreenCondition(
                        new SplashScreen.KeepOnScreenCondition() {
                            @Override
                            public boolean shouldKeepOnScreen() {
                                return keepOn || (System.currentTimeMillis() - startTime <= minSplashTime);
                            }
                        }
                );
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spf = getSharedPreferences("mydata", MODE_PRIVATE);

        locationTracker = new LocationTracker(this, this );

        if(NetworkUtil.isInternetAvailable(this)){
            if (locationTracker.checkPermissions()) {
                getReady();
                locationTracker.startLocationUpdates();
            }else{
                keepOn = false;
            }
        }else{
            keepOn = false;
            Dialog3();
        }

        binding.toolbar.setTitle("");
        binding.toolbarTitle.setText(R.string.app_name);
        binding.toolbarTitle.setSelected(true);

        setSupportActionBar(binding.toolbar);
        Drawable lefticon = ContextCompat.getDrawable(this, R.drawable.bus_icon);
        binding.toolbar.setNavigationIcon(lefticon);
        binding.routeList.setLayoutManager(new LinearLayoutManager(this));
        getDirection = new GetDirection(this);

        bannerListener = new BannerViewListener(this);

        UnityAds.initialize(this, unityGameID, testMode,
                new UnityInitializationListener(this));

        binding.searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    List<Routes> filterlist = new ArrayList<>();

                    for (Routes route : routeslist) {
                        if (route.getRtnm().toLowerCase().contains(s.toString().toLowerCase()) ||
                                route.getRt().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterlist.add(route);
                        }
                    }
                    adapter = new RouteItemAdapter(filterlist, (route, view) -> {
                        getDirection.getData(route,view);
                    });
                    binding.routeList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void getReady() {
        getroutes = new Getroutes(this);
        getroutes.getData();
    }

    public void acceptRoutes(JSONArray routes) {
        keepOn = false;
        routeslist.clear();
        routeslist.addAll(GetRoutesfetcher.getRoutes(routes));
        if(!routeslist.isEmpty()){
            adapter = new RouteItemAdapter(routeslist, (route, view) -> {
                getDirection.getData(route,view);
            });
            binding.routeList.setAdapter(adapter);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void acceptDirection(List<String> list,Routes rt, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, list.get(i));
            }
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Intent intent = new Intent(this, StopsActivity.class);
                intent.putExtra("route", rt.getRt());
                intent.putExtra("direction", menuItem.getTitle());
                intent.putExtra("routename",rt.getRtnm());
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                startActivity(intent);
                return true;
            });

            popupMenu.show();

        } else {
            Toast.makeText(this, "No Direction Found", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rigthsidemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.infobtn) {
            OpenInfoDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    private void OpenInfoDialog() {
        View customView = getLayoutInflater().inflate(R.layout.layout, null);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setView(customView)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setBackground(ContextCompat.getDrawable(this, R.drawable.borderdialogbox));

        final androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        TextView textView = customView.findViewById(R.id.clicktv);
        String url = getString(R.string.devlink);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
        SpannableString spannableString = new SpannableString(url);
        spannableString.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!isLocationEnabled()){
                    Dialog4();
                }else{
                    getReady();
                    locationTracker.startLocationUpdates();
                }

            } else {
                if (spf.getInt("permission", 0) == 0) {
                    spf.edit().putInt("permission", 1).apply();
                } else if (spf.getInt("permission", 0) == 1) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Dialog2();
                    }else{
                        Dialog1();
                    }
                }
            }
        }
    }

    private void Dialog1() {
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.bus_icon);
        if (icon != null) {
            icon.setTintList(ContextCompat.getColorStateList(this, R.color.black));
        }
        new MaterialAlertDialogBuilder(this)
                .setIcon(icon)
                .setTitle("Fine Accuracy Needed")
                .setMessage("This application needs Fine Accuracy permission in order to determine the closest stops bus to your location. It will not function properly without this permission. Will you allow it?")
                .setPositiveButton("YES", (dialog, which) -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 111);
                })
                .setNegativeButton("NO THANKS", (dialog, which) -> {
                    dialog.dismiss();
                    Dialog2();
                })
                .show();
    }


    private void Dialog2() {
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.bus_icon);
        if (icon != null) {
            icon.setTintList(ContextCompat.getColorStateList(this, R.color.black));
        }
        new MaterialAlertDialogBuilder(this)
                .setIcon(icon)
                .setTitle("Fine Accuracy Needed")
                .setMessage("This application needs Fine Accuracy permission in order to determine the closest stops bus to your location. It will not function properly without this permission. Please start the application again and allow this permission.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }


    private void Dialog3() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.app_fullname)
                .setMessage("Unable to contact Bus Tracker API due to network problem. Please check your network connection.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    private void Dialog4() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.app_fullname)
                .setMessage("Unable to determine device location. If this is an emulator, please set a location.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    @Override
    public void onLocationUpdate(String timestamp, String coordinates, String updateInfo) {
        String[] parts = coordinates.split(",");
         lat = Double.parseDouble(parts[0].replaceAll("[^0-9.-]", ""));
         lon = Double.parseDouble(parts[1].replaceAll("[^0-9.-]", ""));
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            return false;
        }

        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        return gpsEnabled || networkEnabled;
    }


    @Override
    public void showBanner() {
        BannerView bottomBanner = new BannerView(
                this, bannerPlacement, UnityBannerSize.getDynamicSize(this));
        bottomBanner.setListener(bannerListener);
        binding.layout.addView(bottomBanner);
        bottomBanner.load();
    }

    @Override
    public void initFailed(String errorMessage) {
        stopProgress();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    public void stopProgress() {
    }

    // If needed, you can add other helper methods:
    public void loadFailed(String errorMessage) {
        stopProgress();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}