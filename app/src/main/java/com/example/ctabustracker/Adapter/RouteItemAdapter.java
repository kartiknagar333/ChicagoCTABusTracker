package com.example.ctabustracker.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ctabustracker.Holder.RouteItemHolder;
import com.example.ctabustracker.Model.Routes;
import com.example.ctabustracker.databinding.RouteItemLayoutBinding;

import java.util.List;

public class RouteItemAdapter extends RecyclerView.Adapter<RouteItemHolder>{

    private List<Routes> routes;
    private OnItemClickListener onItemClickListener;

    public RouteItemAdapter(List<Routes> route,OnItemClickListener onItemClickListner) {
        this.routes = route;
        this.onItemClickListener = onItemClickListner;
    }

    public interface OnItemClickListener {
        void onItemClick(Routes route,View view);
    }

    @NonNull
    @Override
    public RouteItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RouteItemLayoutBinding binding = RouteItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RouteItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteItemHolder holder, int position) {
        Routes route = routes.get(position);

        holder.binding.routeNumber.setText(route.getRt());
        holder.binding.routeName.setText(route.getRtnm());
        holder.binding.routeLayout.setBackgroundColor(route.backgroundColor());
        holder.binding.routeNumber.setTextColor(route.textColor());
        holder.binding.routeName.setTextColor(route.textColor());

        holder.binding.routeLayout.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(route,holder.binding.routeLayout);
            }
        });

    }


    @Override
    public int getItemCount() {
        return routes.size();
    }

}
