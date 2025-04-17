package com.example.ctabustracker.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ctabustracker.API.AlertService;
import com.example.ctabustracker.API.GetStop;
import com.example.ctabustracker.Adapter.StopItemAdapter;
import com.example.ctabustracker.Ads.AdsCallback;
import com.example.ctabustracker.Ads.BannerViewListener;
import com.example.ctabustracker.Ads.UnityInitializationListener;
import com.example.ctabustracker.Model.Stops;
import com.example.ctabustracker.R;
import com.example.ctabustracker.databinding.ActivityMainBinding;
import com.example.ctabustracker.databinding.ActivityStopsBinding;
import com.example.ctabustracker.fetcher.GetStopsfetcher;
import com.google.android.gms.maps.model.LatLng;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import org.json.JSONArray;

public class StopsActivity extends AppCompatActivity implements AdsCallback {

    private ActivityStopsBinding binding;
    private String route,direction,name;
    private StopItemAdapter adapter;
    private double lat,lon;
    private static final String unityGameID = "4921141";
    private static final boolean testMode = true;
    private static final String bannerPlacement = "Banner_Android";
    private BannerView.IListener bannerListener;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityStopsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
             route = bundle.getString("route");
             name = bundle.getString("routename");
             direction = bundle.getString("direction");
             lat = bundle.getDouble("lat");
             lon = bundle.getDouble("lon");
        }
        binding.toolbar.setTitle("");
        binding.toolbarTitle.setText("Route  "+ route + " - " + name);
        binding.toolbarTitle.setSelected(true);

        setSupportActionBar(binding.toolbar);

        AlertService.fetchAlerts(this,"36");

        bannerListener = new BannerViewListener(this);

        UnityAds.initialize(this, unityGameID, testMode,
                new UnityInitializationListener(this));

        binding.tvdir.setText(direction + " Stops");
        Drawable lefticon = ContextCompat.getDrawable(this, R.drawable.bus_icon);
        binding.toolbar.setNavigationIcon(lefticon);
        binding.stopsRlv.setLayoutManager(new LinearLayoutManager(this));
        GetStop getStop = new GetStop(this);
        getStop.getData(route,direction);

    }
    public void acceptStops(JSONArray stops){
        if (stops != null) {
            LatLng newLocation = new LatLng(lat, lon);
            adapter = new StopItemAdapter(GetStopsfetcher.getStops(lat,lon,stops), newLocation,new StopItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Stops stops) {
                    Intent intent = new Intent(StopsActivity.this, PredictionsActivity.class);
                    intent.putExtra("route", route);
                    intent.putExtra("direction", direction);
                    intent.putExtra("routename",name);
                    intent.putExtra("stopname",stops.getStpnm());
                    intent.putExtra("stpid",stops.getStpid());
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    startActivity(intent);
                }
            });
            binding.stopsRlv.setAdapter(adapter);
        }else {
            finish();
            Toast.makeText(this, "Getting Stops Failed!", Toast.LENGTH_SHORT).show();
        }
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