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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.carparkingapp.R;
import com.example.carparkingapp.core.network.ApiClient;
import com.example.carparkingapp.core.network.ApiInterface;
import com.example.carparkingapp.features.auth.login.LoginActivity;
import com.example.carparkingapp.features.maps.adapters.ParkingRecyclerAdapter;
import com.example.carparkingapp.models.ParkingLotDetails;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
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
    private RecyclerView searchSuggestionsList;
    private List<Marker> parkingMarkers;
    private ParkingRecyclerAdapter parkingAdapter;
    private List<ParkingLotDetails> pendingParkingList;

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
        
        // Set up SearchView and RecyclerView
        searchView = findViewById(R.id.mapSearch);
        searchSuggestionsList = findViewById(R.id.searchSuggestionsList);
        
        // Set up RecyclerView
        searchSuggestionsList.setLayoutManager(new LinearLayoutManager(this));
        parkingAdapter = new ParkingRecyclerAdapter(new ArrayList<>());
        searchSuggestionsList.setAdapter(parkingAdapter);
        
        // Set up item click listener
        parkingAdapter.setOnItemClickListener(parking -> {
            if (parking != null) {
                LatLng location = new LatLng(parking.getLatitude(), parking.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
                
                // Find and highlight the marker
                for (Marker marker : parkingMarkers) {
                    if (marker.getTitle() != null && 
                        marker.getTitle().equals(parking.getParking_name())) {
                        marker.showInfoWindow();
                        break;
                    }
                }
                // Hide search suggestions after selection
                searchSuggestionsList.setVisibility(View.GONE);
            }
        });

        // Set up search listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                searchSuggestionsList.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    searchSuggestionsList.setVisibility(View.VISIBLE);
                    parkingAdapter.getFilter().filter(newText);
                } else {
                    searchSuggestionsList.setVisibility(View.GONE);
                }
                return true;
            }
        });

        // Hide suggestions when search view loses focus
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                searchSuggestionsList.setVisibility(View.GONE);
            }
        });
        
        // Start loading data
        loadParkingLots();
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
                        
                        // If we have pending parking data, update the map with it
                        if (pendingParkingList != null) {
                            updateMapWithParking(pendingParkingList);
                            pendingParkingList = null;
                        } else {
                            loadParkingLots();
                        }
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

        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLotDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingLotDetails>> call, @NonNull Response<List<ParkingLotDetails>> response) {
                runOnUiThread(() -> {
                    Log.d(TAG, "HTTP status: " + response.code());
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Parking lots loaded: " + response.body().size());
                        updateMapWithParking(response.body());
                    } else {
                        String errorBody = null;
                        try {
                            errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        } catch (Exception e) {
                            errorBody = "error reading errorBody: " + e.getMessage();
                        }
                        Log.e(TAG, "Response unsuccessful or empty. Error body: " + errorBody);
                        Toast.makeText(DashboardActivity.this, "Could not load parking lots. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingLotDetails>> call, @NonNull Throwable t) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Network error while loading parking lots", t);
                    Toast.makeText(DashboardActivity.this, "Could not load parking lots. Please check your connection.", Toast.LENGTH_SHORT).show();
                });
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

        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLotDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingLotDetails>> call, @NonNull Response<List<ParkingLotDetails>> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ParkingLotDetails> allLocations = response.body();
                        List<ParkingLotDetails> nearbyLocations = new ArrayList<>();
                        
                        // Filter locations within 5km radius
                        for (ParkingLotDetails location : allLocations) {
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
                            "Failed to fetch parking locations", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingLotDetails>> call, @NonNull Throwable t) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error fetching parking locations: " + t.getMessage());
                    Toast.makeText(DashboardActivity.this,
                        "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void updateMapWithParking(List<ParkingLotDetails> parkingList) {
        // Update adapter with new data
        if (parkingList != null) {
            runOnUiThread(() -> {
                parkingAdapter.updateLocations(parkingList);
                // Show search suggestions list if adapter is not empty
                if (!parkingList.isEmpty()) {
                    searchView.setQueryHint("Found " + parkingList.size() + " parking locations");
                }
            });
        }
        
        // Check if map is ready
        if (mMap == null) {
            Log.d(TAG, "Map not ready yet, storing parking data for later");
            // Store the parking list to be added when map is ready
            pendingParkingList = parkingList;
            return;
        }
        
        // Clear existing markers
        for (Marker marker : parkingMarkers) {
            marker.remove();
        }
        parkingMarkers.clear();

        // Add new markers
        for (ParkingLotDetails parking : parkingList) {
            LatLng position = new LatLng(parking.getLatitude(), parking.getLongitude());
            // Use a custom vivid green marker icon
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green); // Ensure ic_marker_green.png exists in res/drawable
            String snippet = "Available: " + parking.getAvailableSlots() + "/" + parking.getTotalSlots() + " slots";
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(parking.getParking_name())
                    .snippet(snippet)
                    .icon(icon)
            );
            if (marker != null) {
                parkingMarkers.add(marker);
            }
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
