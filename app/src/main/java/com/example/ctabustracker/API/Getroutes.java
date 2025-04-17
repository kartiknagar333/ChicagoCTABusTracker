package com.example.ctabustracker.API;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ctabustracker.Activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Getroutes {
    private static final String URL = "https://www.ctabustracker.com/bustime/api/v2/getroutes?key=WXkE46uwciZa76Q86PghSDxBA&format=json";
    private final MainActivity mainActivity;
    private JSONArray routes = null;

    public Getroutes(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getData() {
        if (isCachedData()) {
            return;
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        routes = response.getJSONObject("bustime-response").getJSONArray("routes");
                        mainActivity.runOnUiThread(() -> mainActivity.acceptRoutes(routes));
                    } catch (JSONException e) {
                        mainActivity.runOnUiThread(() -> mainActivity.acceptRoutes(null));
                    }
                },
                error -> {
                    mainActivity.runOnUiThread(() -> mainActivity.acceptRoutes(null));
                }) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String jsonString = new String(
                                    response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "UTF-8")
                            );
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

        MyVolley.getInstance(mainActivity).addToRequestQueue(jsonObjectRequest);
    }

    private boolean isCachedData() {
        com.android.volley.Cache.Entry entry = MyVolley
                .getInstance(mainActivity)
                .getRequestQueue()
                .getCache()
                .get(URL);

        if (entry != null && System.currentTimeMillis() < entry.softTtl) {
            try {
                String jsonString = new String(entry.data, "UTF-8");
                org.json.JSONObject response = new org.json.JSONObject(jsonString);
                org.json.JSONArray routes = response
                        .getJSONObject("bustime-response")
                        .getJSONArray("routes");

                mainActivity.runOnUiThread(() -> mainActivity.acceptRoutes(routes));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


}
