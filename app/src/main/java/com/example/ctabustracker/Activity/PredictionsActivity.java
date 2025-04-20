package com.example.ctabustracker.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ctabustracker.API.GetBusInfo;
import com.example.ctabustracker.API.GetPrediction;
import com.example.ctabustracker.Adapter.PredictionItemAdapter;
import com.example.ctabustracker.Ads.AdsCallback;
import com.example.ctabustracker.Ads.BannerViewListener;
import com.example.ctabustracker.Ads.UnityInitializationListener;
import com.example.ctabustracker.Model.PredictionBus;
import com.example.ctabustracker.R;
import com.example.ctabustracker.databinding.ActivityPredictionsBinding;
import com.example.ctabustracker.fetcher.GetPredictionfetcher;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.maps.android.SphericalUtil;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PredictionsActivity extends AppCompatActivity  implements AdsCallback {
    private ActivityPredictionsBinding binding;
    private String route,name,direction,stopname,stpid;
    private PredictionItemAdapter adapter;
    private GetBusInfo getBusInfo;
    private double lat,lon;
    private int duetime;
    private static final String unityGameID = "4921141";
    private static final boolean testMode = true;
    private static final String bannerPlacement = "Banner_Android";
    private BannerView.IListener bannerListener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityPredictionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            route = bundle.getString("route");
            name = bundle.getString("routename");
            direction = bundle.getString("direction");
            stopname = bundle.getString("stopname");
            stpid = bundle.getString("stpid");
            lat = bundle.getDouble("lat");
            lon = bundle.getDouble("lon");
        }


        binding.toolbar.setTitle("");
        binding.toolbarTitle.setText("Route  "+ route + " - " + name);
        binding.toolbarTitle.setSelected(true);
        setSupportActionBar(binding.toolbar);
        binding.tvdir.setText(stopname + " (" + direction+ ") \n" + updateTime() );
        Drawable lefticon = ContextCompat.getDrawable(this, R.drawable.bus_icon);
        binding.toolbar.setNavigationIcon(lefticon);
        bannerListener = new BannerViewListener(this);

        UnityAds.initialize(this, unityGameID, testMode,
                new UnityInitializationListener(this));

        binding.stopsRlv.setLayoutManager(new LinearLayoutManager(this));
        GetPrediction getPrediction = new GetPrediction(this);
        getPrediction.getData(route,stpid);
        getBusInfo = new GetBusInfo(this);

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.tvdir.setText(stopname + " (" + direction+ ") \n" + updateTime() );
                getPrediction.getData(route,stpid);
            }
        });

    }

    private String updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return sdf.format(new Date());
    }

    @SuppressLint("SetTextI18n")
    public void acceptprediction(JSONArray prd) {
        if(prd != null){
            if (binding.swiperefresh.isRefreshing()) {
                binding.swiperefresh.setRefreshing(false);
            }
            adapter = new PredictionItemAdapter(GetPredictionfetcher.getPredictionBusList(prd), new PredictionItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PredictionBus bus) {
                    duetime = bus.getPrdctdn().equals("DUE") ? 0 : Integer.parseInt(bus.getPrdctdn());
                    getBusInfo.getData(bus.getVid());
                }
            });
            binding.stopsRlv.setAdapter(adapter);
        }else{
            finish();
            Toast.makeText(this, "Getting Predictions Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void acceptBusinfo(JSONObject businfo) {
        if(businfo != null){
            LatLng userLocation = new LatLng(lat, lon);
            LatLng targetLocation = null;
            try {
                double tarlat = Double.parseDouble(businfo.getString("lat"));
                double tarlon = Double.parseDouble(businfo.getString("lon"));
                targetLocation = new LatLng(tarlat, tarlon);
                double distanceMeters = SphericalUtil.computeDistanceBetween(userLocation, targetLocation);
                String busnumber = "Bus #" + businfo.getString("vid");
                String uri = "https://www.google.com/maps/search/?api=1&query=" + tarlat + "," + tarlon;
                String msg = busnumber + " is " + String.format(Locale.getDefault(), "%.0f meters (" + duetime + " min) away from the " + stopname + " stop", distanceMeters);
                opendialog(busnumber,msg,uri);
            } catch (JSONException e) {

            }
        }else{
            finish();
            Toast.makeText(this, "Getting Bus Info Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void opendialog(String num,String msg,String uri) {
        View customView = getLayoutInflater().inflate(R.layout.bus_info_dialog, null);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setView(customView)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.borderdialogbox));

        final androidx.appcompat.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button btnmap = customView.findViewById(R.id.btnmap);
        Button btnok = customView.findViewById(R.id.btnok);
        TextView busnum = customView.findViewById(R.id.busnum);
        busnum.setText(num);
        TextView businfo = customView.findViewById(R.id.businfo);
        businfo.setText(msg);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void accepterror(String msg) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.app_fullname)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
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