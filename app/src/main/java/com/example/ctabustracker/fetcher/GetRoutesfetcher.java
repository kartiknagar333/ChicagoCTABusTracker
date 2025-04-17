package com.example.ctabustracker.fetcher;

import com.example.ctabustracker.Model.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetRoutesfetcher {
    private static volatile GetRoutesfetcher instance = null;
    private static final List<Routes> routes = new ArrayList<>();

    private GetRoutesfetcher() {
        final ObjectMapper mapper = new ObjectMapper();
    }

    public static GetRoutesfetcher getInstance() {
        if (instance == null) {
            synchronized (GetRoutesfetcher.class) {
                if (instance == null) {
                    instance = new GetRoutesfetcher();
                }
            }
        }
        return instance;
    }


    public static List<Routes> getRoutes(final JSONArray route) {
        routes.clear();
        for (int i = 0; i < route.length(); i++) {
            try {
                JSONObject jsonObject = route.getJSONObject(i);
                Routes routeObject = new Routes(jsonObject.getString("rt"),
                        jsonObject.getString("rtnm"),
                        jsonObject.getString("rtclr"),
                        jsonObject.getString("rtdd"));
                routes.add(routeObject);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return routes;
    }


}
