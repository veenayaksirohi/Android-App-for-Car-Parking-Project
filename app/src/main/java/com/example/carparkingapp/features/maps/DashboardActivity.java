package com.example.carparkingapp.features.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.carparkingapp.R;
import com.example.carparkingapp.core.network.ApiClient;
import com.example.carparkingapp.core.network.ApiInterface;
import com.example.carparkingapp.features.auth.login.LoginActivity;
import com.example.carparkingapp.models.ParkingLocation;
import com.example.carparkingapp.utils.TokenManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "DashboardActivity";
    private GoogleMap mMap;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private List<Marker> parkingMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        // Initialize variables
        parkingMarkers = new ArrayList<>();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Set up map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        
        // Set up search view
        searchView = findViewById(R.id.mapSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        // Check and request location permissions if needed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        // Enable location features
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Get last known location and center map
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        lastKnownLocation = location;
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        loadParkingLots();
                    }
                });
    }

    private void loadParkingLots() {
        String token = TokenManager.getAuthHeader(this);
        if (token == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        Log.d(TAG, "Loading parking lots with token: " + token);

        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingLocation>> call, @NonNull Response<List<ParkingLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateMapWithParking(response.body());
                } else {
                    Toast.makeText(DashboardActivity.this, 
                        "Failed to load parking lots", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingLocation>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to load parking lots: " + t.getMessage());
                Toast.makeText(DashboardActivity.this,
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng target = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 15));
                fetchParkingLocations(target);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error searching location: " + e.getMessage());
            Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchParkingLocations(LatLng targetLocation) {
        if (targetLocation == null) return;

        String token = TokenManager.getAuthHeader(this);
        if (token == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingLocation>> call, @NonNull Response<List<ParkingLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ParkingLocation> allLocations = response.body();
                    List<ParkingLocation> nearbyLocations = new ArrayList<>();
                    
                    // Filter locations within 5km radius
                    for (ParkingLocation location : allLocations) {
                        double distance = calculateDistance(
                            targetLocation.latitude, targetLocation.longitude,
                            location.getLatitude(), location.getLongitude()
                        );
                        if (distance <= 5) { // 5km radius
                            nearbyLocations.add(location);
                        }
                    }
                    
                    updateMapWithParking(nearbyLocations);
                } else {
                    Toast.makeText(DashboardActivity.this,
                        "Failed to load parking lots", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingLocation>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to load parking lots: " + t.getMessage());
                Toast.makeText(DashboardActivity.this,
                    "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMapWithParking(List<ParkingLocation> parkingList) {
        // Clear existing markers
        for (Marker marker : parkingMarkers) {
            marker.remove();
        }
        parkingMarkers.clear();

        // Add new markers
        for (ParkingLocation parking : parkingList) {
            LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());
            
            // Calculate color based on availability
            double availabilityPercentage = (double) parking.getAvailableSlots() / parking.getTotalSlots() * 100;
            float markerColor = getMarkerColor(availabilityPercentage);
            
            // Create and add marker
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(parking.getParkingName())
                    .snippet("Available: " + parking.getAvailableSlots() + "/" + parking.getTotalSlots())
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            
            if (marker != null) {
                parkingMarkers.add(marker);
            }
        }
    }

    private float getMarkerColor(double availabilityPercentage) {
        if (availabilityPercentage > 50) {
            return BitmapDescriptorFactory.HUE_GREEN;  // Plenty of space
        } else if (availabilityPercentage > 20) {
            return BitmapDescriptorFactory.HUE_YELLOW; // Getting full
        } else {
            return BitmapDescriptorFactory.HUE_RED;    // Almost full
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
