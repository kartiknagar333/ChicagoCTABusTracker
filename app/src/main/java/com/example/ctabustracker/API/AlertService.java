package com.example.ctabustracker.API;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertService {

    private static final String API_URL = "https://www.transitchicago.com/api/1.0/alerts.aspx?routeid=";

    private static String getApiUrl(String routeId){
        return API_URL + routeId + "&activeonly=true&outputType=JSON";
    }
    public static void fetchAlerts(Context context, String selectedRouteId) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, getApiUrl(selectedRouteId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("API_RESPONSE", getApiUrl(selectedRouteId));  // <-- Log entire response

                            Log.d("API_RESPONSE", response.toString());  // <-- Log entire response

                            JSONObject ctaAlerts = response.getJSONObject("CTAAlerts");
                            JSONArray alerts = ctaAlerts.getJSONArray("Alert");

                            for (int i = 0; i < alerts.length(); i++) {
                                JSONObject alert = alerts.getJSONObject(i);
                                String alertId = alert.getString("AlertId");
                                String title = alert.getString("Headline");
                                String shortDescription = alert.getString("ShortDescription");
                                String fullDescription = alert.getJSONObject("FullDescription")
                                        .getString("#cdata-section");
                                String alertURL = alert.getJSONObject("AlertURL")
                                        .getString("#cdatasection");

                                // Get the ServiceId for the impacted route
                                String impactedRouteId = alert.getJSONObject("ImpactedService")
                                        .getJSONObject("Service")
                                        .getString("ServiceId");

                                // Show alert only if it matches the selected route
                                if (impactedRouteId.equals(selectedRouteId) && !wasAlertDisplayed(context, alertId)) {
                                    showAlertDialog(context, title, shortDescription, fullDescription, alertURL);
                                    markAlertAsDisplayed(context, alertId);
                                }
                            }

                        } catch (JSONException e) {
                            Log.e("AlertService", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AlertService", "API request error: " + error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private static void showAlertDialog(Context context, String title, String shortDescription, String fullDescription, String alertURL) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // Convert HTML content to Spanned for formatting
        Spanned formattedDescription = Html.fromHtml(shortDescription + "<br><br>" + fullDescription);

        // Create a Scrollable TextView to display the alert message properly
        TextView messageView = new TextView(context);
        messageView.setText(formattedDescription);
        messageView.setPadding(50, 30, 50, 30);
        messageView.setTextSize(16);

        // Wrap it inside a ScrollView to handle long texts
        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(messageView);

        builder.setView(scrollView);
        builder.setPositiveButton("OK", null);
        builder.setNeutralButton("More Info", (dialog, which) -> {
            openWebPage(context, alertURL);
        });

        builder.show();
    }

    private static void openWebPage(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private static void markAlertAsDisplayed(Context context, String alertId) {
        context.getSharedPreferences("Alerts", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(alertId, true)
                .apply();
    }

    private static boolean wasAlertDisplayed(Context context, String alertId) {
        return context.getSharedPreferences("Alerts", Context.MODE_PRIVATE)
                .getBoolean(alertId, false);
    }
}
