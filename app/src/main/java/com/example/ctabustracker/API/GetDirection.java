package com.example.ctabustracker.API;

import android.view.View;
import android.widget.Toast;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ctabustracker.Activity.MainActivity;
import com.example.ctabustracker.Model.Routes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GetDirection {
    private static final String URL = "https://www.ctabustracker.com/bustime/api/v2/getdirections?key=WXkE46uwciZa76Q86PghSDxBA&format=json&rt=";
    private final MainActivity mainActivity;
    private JSONArray direction = null;
    private List<String> list = new ArrayList<>();

    public GetDirection(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private String getURL(String route) {
        return URL + route;
    }

    public void getData(final Routes route, final View view) {
        final String fullUrl = getURL(route.getRt());
        if (isCachedData(fullUrl, route, view)) {
            return;
        }
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                fullUrl,
                null,
                response -> {
                    try {
                        direction = response.getJSONObject("bustime-response").getJSONArray("directions");
                        list.clear();
                        for (int i = 0; i < direction.length(); i++) {
                            list.add(direction.getJSONObject(i).getString("dir"));
                        }
                        mainActivity.runOnUiThread(() -> mainActivity.acceptDirection(list, route, view));
                    } catch (JSONException e) {
                        mainActivity.runOnUiThread(() -> mainActivity.acceptDirection(null, route, view));
                    }
                },
                error -> mainActivity.runOnUiThread(() -> mainActivity.acceptDirection(null, route, view))) {
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
        MyVolley.getInstance(mainActivity).addToRequestQueue(jsonObjectRequest);
    }

    private boolean isCachedData(String url, Routes route, View view) {
        Cache.Entry entry = MyVolley.getInstance(mainActivity).getRequestQueue().getCache().get(url);
        if (entry != null && System.currentTimeMillis() < entry.softTtl) {
            try {
                String jsonString = new String(entry.data, "UTF-8");
                JSONObject response = new JSONObject(jsonString);
                JSONArray directions = response.getJSONObject("bustime-response").getJSONArray("directions");
                list.clear();
                for (int i = 0; i < directions.length(); i++) {
                    list.add(directions.getJSONObject(i).getString("dir"));
                }
                mainActivity.runOnUiThread(() -> mainActivity.acceptDirection(list, route, view));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
