package com.example.ctabustracker.API;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ctabustracker.Activity.PredictionsActivity;
import com.example.ctabustracker.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetBusInfo {
    private static final String URL = "https://www.ctabustracker.com/bustime/api/v2/getvehicles?key="+
            BuildConfig.API_KEY +"&format=json&vid=";
    private final PredictionsActivity predictionsActivity;
    private JSONObject object = null;

    public GetBusInfo(final PredictionsActivity predictionsActivity) {
        this.predictionsActivity = predictionsActivity;
    }

    private String getURL(final String vid) {
        return URL + vid;
    }
    public void getData(final String vid) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getURL(vid),
                null,
                response -> {
                    try {
                        object = null;
                        object = response.getJSONObject("bustime-response").getJSONArray("vehicle").getJSONObject(0);
                        predictionsActivity.runOnUiThread(() -> predictionsActivity.acceptBusinfo(object));
                    } catch (JSONException e) {
                        predictionsActivity.runOnUiThread(() -> predictionsActivity.acceptBusinfo(null));
                    }
                },
                error -> {
                    predictionsActivity.runOnUiThread(() -> predictionsActivity.acceptBusinfo(null));
                }) {

        };

        MyVolley.getInstance(predictionsActivity).addToRequestQueue(jsonObjectRequest);
    }


}
