package com.example.ctabustracker.fetcher;

import com.example.ctabustracker.Model.Routes;
import com.example.ctabustracker.Model.Stops;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetStopsfetcher {
    private static LatLng userLocation;
    private static volatile GetStopsfetcher instance = null;
    private static final List<Stops> stoplist = new ArrayList<>();

    private GetStopsfetcher() {
        final ObjectMapper mapper = new ObjectMapper();
    }

    public static GetStopsfetcher getInstance() {
        if (instance == null) {
            synchronized (GetStopsfetcher.class) {
                if (instance == null) {
                    instance = new GetStopsfetcher();
                }
            }
        }
        return instance;
    }


    public static List<Stops> getStops(final double latt, final double lonn, final JSONArray stops) {
        stoplist.clear();
        userLocation = new LatLng(latt, lonn);
        for (int i = 0; i < stops.length(); i++) {
            try {
                JSONObject ob = stops.getJSONObject(i);
                //If you want get stops in 1000m radius from the user location, you can edit the if condition.
                if (check1000m(new LatLng(ob.getDouble("lat"), ob.getDouble("lon"))) > 0){
                    Stops stop = new Stops(ob.getString("stpid"),
                            ob.getString("stpnm"),
                            ob.getDouble("lat"),
                            ob.getDouble("lon"));

                    stoplist.add(stop);
                }

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return stoplist;
    }

    public static Double check1000m(LatLng targetLocation) {
        return SphericalUtil.computeDistanceBetween(userLocation, targetLocation);

    }


}
