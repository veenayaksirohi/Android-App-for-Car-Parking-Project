package com.example.carparkingapp.features.maps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carparkingapp.R;
import com.example.carparkingapp.models.ParkingLotDetails;

import java.util.ArrayList;
import java.util.List;

public class ParkingLocationAdapter extends ArrayAdapter<ParkingLotDetails> implements Filterable {
    private final List<ParkingLotDetails> allLocations;
    private List<ParkingLotDetails> filteredLocations;
    private final LayoutInflater inflater;

    public ParkingLocationAdapter(Context context, List<ParkingLotDetails> locations) {
        super(context, 0, locations);
        this.allLocations = new ArrayList<>(locations);
        this.filteredLocations = new ArrayList<>(locations);
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_parking_suggestion, parent, false);
        }

        ParkingLotDetails parking = getItem(position);
        if (parking != null) {
            TextView parkingName = convertView.findViewById(R.id.parkingNameTextView);
            TextView address = convertView.findViewById(R.id.addressTextView);
            TextView slots = convertView.findViewById(R.id.availableSlotsTextView);
            TextView charge = convertView.findViewById(R.id.parkingChargeTextView);

            parkingName.setText(parking.getParking_name());
            address.setText(parking.getAddress());
            String availability = String.format("Available: %d/%d slots", 
                parking.getAvailable_car_slots(), parking.getCar_capacity());
            slots.setText(availability);
            charge.setText(String.format("₹%s/hr", parking.getCar_parking_charge()));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return filteredLocations.size();
    }

    @Nullable
    @Override
    public ParkingLotDetails getItem(int position) {
        return filteredLocations.get(position);
    }

    @NonNull
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
                    for (ParkingLotDetails location : allLocations) {
                        if (location.getParking_name().toLowerCase().contains(filterPattern) ||
                            location.getAddress().toLowerCase().contains(filterPattern) ||
                            location.getLandmark().toLowerCase().contains(filterPattern)) {
                            suggestions.add(location);
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

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((ParkingLotDetails) resultValue).getParking_name();
            }
        };
    }
}
