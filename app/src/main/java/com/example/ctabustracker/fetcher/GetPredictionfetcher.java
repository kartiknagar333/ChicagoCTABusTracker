package com.example.ctabustracker.fetcher;

import com.example.ctabustracker.Model.PredictionBus;
import com.example.ctabustracker.Model.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPredictionfetcher {
    private static volatile GetPredictionfetcher instance = null;
    private static final List<PredictionBus> predictionBusList = new ArrayList<>();

    private GetPredictionfetcher() {
        final ObjectMapper mapper = new ObjectMapper();
    }

    public static GetPredictionfetcher getInstance() {
        if (instance == null) {
            synchronized (GetPredictionfetcher.class) {
                if (instance == null) {
                    instance = new GetPredictionfetcher();
                }
            }
        }
        return instance;
    }


    public static List<PredictionBus> getPredictionBusList(final JSONArray prd) {
        predictionBusList.clear();
        for (int i = 0; i < prd.length(); i++) {
            try {
                JSONObject jsonObject = prd.getJSONObject(i);
                PredictionBus routeObject = new PredictionBus(
                        jsonObject.getString("tmstmp"),
                        jsonObject.getString("typ"),
                        jsonObject.getString("stpnm"),
                        jsonObject.getString("stpid"),
                        jsonObject.getString("vid"),
                        jsonObject.getInt("dstp"),
                        jsonObject.getString("rt"),
                        jsonObject.getString("rtdd"),
                        jsonObject.getString("rtdir"),
                        jsonObject.getString("des"),
                        jsonObject.getString("prdtm"),
                        jsonObject.getString("tablockid"),
                        jsonObject.getString("tatripid"),
                        jsonObject.getString("origtatripno"),
                        jsonObject.getBoolean("dly"),
                        jsonObject.getString("prdctdn"),
                        jsonObject.getString("zone"));
                predictionBusList.add(routeObject);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return predictionBusList;
    }


}
