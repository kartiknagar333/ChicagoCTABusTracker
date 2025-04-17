package com.example.ctabustracker.Holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.databinding.RouteItemLayoutBinding;

public class RouteItemHolder extends RecyclerView.ViewHolder {
    public RouteItemLayoutBinding binding;
    public RouteItemHolder(@NonNull RouteItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
