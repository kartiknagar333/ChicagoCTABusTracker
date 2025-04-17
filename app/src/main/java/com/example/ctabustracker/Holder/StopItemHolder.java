package com.example.ctabustracker.Holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.databinding.RouteItemLayoutBinding;
import com.example.ctabustracker.databinding.StopItemLayoutBinding;

public class StopItemHolder extends RecyclerView.ViewHolder {
    public StopItemLayoutBinding binding;
    public StopItemHolder(@NonNull StopItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
