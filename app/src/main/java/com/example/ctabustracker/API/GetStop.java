package com.example.ctabustracker.API;


import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ctabustracker.Activity.StopsActivity;
import com.example.ctabustracker.BuildConfig;
import com.example.ctabustracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class GetStop {
    private static final String URL = "https://www.ctabustracker.com/bustime/api/v2/getstops?key="+
            BuildConfig.API_KEY +"&format=json&rt=";
    private final StopsActivity stopsActivity;
    private JSONArray stoparray = null;

    public GetStop(final StopsActivity stopsActivity) {
        this.stopsActivity = stopsActivity;
    }

    private String getURL(String number, String name) {
        return URL + number + "&dir=" + name;
    }

    public void getData(String number, String name) {
        final String fullUrl = getURL(number, name);
        if (isCachedData(fullUrl)) {
            return;
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                fullUrl,
                null,
                response -> {
                    try {
                        stoparray = response.getJSONObject("bustime-response").getJSONArray("stops");
                        stopsActivity.runOnUiThread(() -> stopsActivity.acceptStops(stoparray));
                    } catch (JSONException e) {
                        stopsActivity.runOnUiThread(() -> stopsActivity.acceptStops(null));
                    }
                },
                error -> stopsActivity.runOnUiThread(() -> stopsActivity.acceptStops(null))) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "UTF-8"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long now = System.currentTimeMillis();
                    final long cacheExpire = now + 24 * 60 * 60 * 1000;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = cacheExpire;
                    cacheEntry.ttl = cacheExpire;
                    return Response.success(jsonResponse, cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        MyVolley.getInstance(stopsActivity).addToRequestQueue(jsonObjectRequest);
    }

    private boolean isCachedData(String url) {
        Cache.Entry entry = MyVolley.getInstance(stopsActivity).getRequestQueue().getCache().get(url);
        if (entry != null && System.currentTimeMillis() < entry.softTtl) {
            try {
                String jsonString = new String(entry.data, "UTF-8");
                JSONObject response = new JSONObject(jsonString);
                stoparray = response.getJSONObject("bustime-response").getJSONArray("stops");
                stopsActivity.runOnUiThread(() -> stopsActivity.acceptStops(stoparray));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
