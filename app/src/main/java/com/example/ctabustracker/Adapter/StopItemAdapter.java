package com.example.ctabustracker.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.Holder.RouteItemHolder;
import com.example.ctabustracker.Holder.StopItemHolder;
import com.example.ctabustracker.Model.Routes;
import com.example.ctabustracker.Model.Stops;
import com.example.ctabustracker.databinding.RouteItemLayoutBinding;
import com.example.ctabustracker.databinding.StopItemLayoutBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;
import java.util.Locale;

public class StopItemAdapter extends RecyclerView.Adapter<StopItemHolder>{

    private List<Stops> stopsList;
    private OnItemClickListener onItemClickListener;
    private LatLng userLocation;

    public StopItemAdapter(List<Stops> stopsList,LatLng userLocation, OnItemClickListener onItemClickListner) {
        this.stopsList = stopsList;
        this.userLocation = userLocation;
        this.onItemClickListener = onItemClickListner;
    }

    public interface OnItemClickListener {
        void onItemClick(Stops stops);
    }

    @NonNull
    @Override
    public StopItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StopItemLayoutBinding binding = StopItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StopItemHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StopItemHolder holder, int position) {
        Stops st = stopsList.get(position);

        holder.binding.tv1.setText(st.getStpnm());

        if (userLocation != null) {
            LatLng stopLocation = new LatLng(st.getLat(), st.getLon());
            String distanceInfo = getDistanceAndDirection(userLocation, stopLocation);
            holder.binding.tv2.setText(distanceInfo);
        } else {
            holder.binding.tv2.setText("Calculating...");
        }

        holder.binding.stoplayout.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(st);
            }
        });

    }


    @Override
    public int getItemCount() {
        return stopsList.size();
    }

    public String getDistanceAndDirection(LatLng userLocation, LatLng targetLocation) {
        double distanceMeters = SphericalUtil.computeDistanceBetween(userLocation, targetLocation);

        double heading = SphericalUtil.computeHeading(userLocation, targetLocation);
        String direction = getDir((float) heading);

        return String.format(Locale.getDefault(), "%.0f m %s of your location", distanceMeters, direction);
    }

    private String getDir(float degIn) {
        int deg = 45 * (Math.round(degIn / 45));
        switch (deg) {
            case 0: return "N";
            case 45: return "NE";
            case 90: return "E";
            case 135: return "SE";
            case 180: return "S";
            case 225: return "SW";
            case 270: return "W";
            case 315: return "NW";
            default: return "Unknown";
        }
    }
}
