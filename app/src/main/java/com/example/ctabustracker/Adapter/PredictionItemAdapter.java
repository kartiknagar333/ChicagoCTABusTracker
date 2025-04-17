package com.example.ctabustracker.Adapter;

import android.annotation.SuppressLint;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.Holder.BusItemHolder;
import com.example.ctabustracker.Holder.StopItemHolder;
import com.example.ctabustracker.Model.PredictionBus;
import com.example.ctabustracker.Model.Stops;
import com.example.ctabustracker.databinding.BusItemLayoutBinding;
import com.example.ctabustracker.databinding.StopItemLayoutBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PredictionItemAdapter extends RecyclerView.Adapter<BusItemHolder>{

    private List<PredictionBus> predictionBusList;
    private OnItemClickListener onItemClickListener;

    public PredictionItemAdapter(List<PredictionBus> predictionBusList, OnItemClickListener onItemClickListner) {
        this.predictionBusList = predictionBusList;
        this.onItemClickListener = onItemClickListner;
    }

    public interface OnItemClickListener {
        void onItemClick(PredictionBus bus);
    }

    @NonNull
    @Override
    public BusItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BusItemLayoutBinding binding = BusItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BusItemHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BusItemHolder holder, int position) {
        PredictionBus bus = predictionBusList.get(position);

        holder.binding.busname.setText("Bus #" + bus.getVid());
        holder.binding.busdue.setText("Due in " + bus.getPrdctdn() + " mins at ");
        holder.binding.busdir.setText(bus.getRtdir() + " to " + bus.getDes());
        holder.binding.bustime.setText(getmaketime(bus.getPrdtm()).toUpperCase());

        holder.binding.buslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bus);
                }
            }
        });


    }

    private String getmaketime(String inputTime) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(inputTime, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a ");
            return dateTime.format(outputFormatter);

        } catch (DateTimeParseException e) {
            return inputTime.split(" ")[1];
        }
    }

    @Override
    public int getItemCount() {
        return predictionBusList.size();
    }

}
