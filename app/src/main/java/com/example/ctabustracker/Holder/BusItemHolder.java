package com.example.ctabustracker.Holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.databinding.BusItemLayoutBinding;
import com.example.ctabustracker.databinding.StopItemLayoutBinding;

public class BusItemHolder extends RecyclerView.ViewHolder {
    public BusItemLayoutBinding binding;
    public BusItemHolder(@NonNull BusItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
