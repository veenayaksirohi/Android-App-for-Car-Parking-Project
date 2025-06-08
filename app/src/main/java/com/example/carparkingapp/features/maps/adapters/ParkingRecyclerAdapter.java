package com.example.carparkingapp.features.maps.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkingapp.R;
import com.example.carparkingapp.models.ParkingLotDetails;

import java.util.ArrayList;
import java.util.List;

public class ParkingRecyclerAdapter extends RecyclerView.Adapter<ParkingRecyclerAdapter.ViewHolder> implements Filterable {
    private final List<ParkingLotDetails> allLocations;
    private List<ParkingLotDetails> filteredLocations;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ParkingLotDetails parking);
    }

    public ParkingRecyclerAdapter(List<ParkingLotDetails> locations) {
        this.allLocations = new ArrayList<>(locations);
        this.filteredLocations = new ArrayList<>(locations);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateLocations(List<ParkingLotDetails> newLocations) {
        allLocations.clear();
        allLocations.addAll(newLocations);
        filteredLocations.clear();
        filteredLocations.addAll(newLocations);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingLotDetails parking = filteredLocations.get(position);
        holder.bind(parking, listener);
    }

    @Override
    public int getItemCount() {
        return filteredLocations.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<ParkingLotDetails> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(allLocations);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ParkingLotDetails parking : allLocations) {
                        // Match by name, address, or landmark
                        if (parking.getParking_name().toLowerCase().contains(filterPattern) ||
                            parking.getAddress().toLowerCase().contains(filterPattern) ||
                            (parking.getLandmark() != null && 
                             parking.getLandmark().toLowerCase().contains(filterPattern))) {
                            suggestions.add(parking);
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredLocations.clear();
                if (results.values != null) {
                    //noinspection unchecked
                    filteredLocations.addAll((List<ParkingLotDetails>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView parkingNameTextView;
        private final TextView addressTextView;
        private final TextView availableSlotsTextView;
        private final TextView parkingChargeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingNameTextView = itemView.findViewById(R.id.parkingNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            availableSlotsTextView = itemView.findViewById(R.id.availableSlotsTextView);
            parkingChargeTextView = itemView.findViewById(R.id.parkingChargeTextView);
        }

        public void bind(final ParkingLotDetails parking, final OnItemClickListener listener) {
            parkingNameTextView.setText(parking.getParking_name());
            addressTextView.setText(parking.getAddress());
            
            // Format and display available slots
            String availability = String.format("Available: %d/%d slots", 
                parking.getAvailableSlots(), parking.getTotalSlots());
            availableSlotsTextView.setText(availability);
            
            // Format and display parking charge
            String charge = String.format("₹%s/hr", parking.getCar_parking_charge());
            parkingChargeTextView.setText(charge);

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(parking);
                }
            });
        }
    }
}
