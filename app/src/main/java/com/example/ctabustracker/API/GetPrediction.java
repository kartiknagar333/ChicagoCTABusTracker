package com.example.ctabustracker.API;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ctabustracker.Activity.MainActivity;
import com.example.ctabustracker.Activity.PredictionsActivity;
import com.example.ctabustracker.Model.Routes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class GetPrediction {
    private static final String URL = "https://www.ctabustracker.com/bustime/api/v2/getpredictions?key=WXkE46uwciZa76Q86PghSDxBA&format=json&rt=";
    private final PredictionsActivity predictionsActivity;
    private JSONArray prd = null;

    public GetPrediction(final PredictionsActivity predictionsActivity) {
        this.predictionsActivity = predictionsActivity;
    }

    private String getURL(final String rt,final String stpid) {
        return URL + rt + "&stpid=" + stpid;
    }
    public void getData(final String rt,final String stpid) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getURL(rt,stpid),
                null,
                response -> {
                    try {
                        prd = null;
                        prd = response.getJSONObject("bustime-response").getJSONArray("prd");
                        predictionsActivity.runOnUiThread(() -> predictionsActivity.acceptprediction(prd));
                    } catch (JSONException e) {
                        predictionsActivity.accepterror("There is no prediction for this stop!");
                    }
                },
                error -> {
                    predictionsActivity.runOnUiThread(() -> predictionsActivity.accepterror(error.getLocalizedMessage()));
                }) {

        };

        MyVolley.getInstance(predictionsActivity).addToRequestQueue(jsonObjectRequest);
    }


}
