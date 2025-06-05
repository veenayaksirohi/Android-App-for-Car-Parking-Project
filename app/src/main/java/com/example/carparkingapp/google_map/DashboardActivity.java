package com.example.carparkingapp.google_map;

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
import com.example.carparkingapp.api.ApiClient;
import com.example.carparkingapp.api.ApiInterface;
import com.example.carparkingapp.login.LoginActivity;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "DashboardActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SearchView searchView;
    private LatLng currentLocation;
    private LatLng searchedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchView = findViewById(R.id.mapSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }
        
        loadParkingLots();
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (searchedLocation == null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        fetchParkingLocations(currentLocation);
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void loadParkingLots() {
        String token = TokenManager.getAuthHeader(this);
        if (token == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        Log.d(TAG, "Loading parking lots with token: " + token);        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLocation>>() {
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

    private void fetchParkingLocations(LatLng targetLocation) {
        if (targetLocation == null) return;

        String token = TokenManager.getAuthHeader(this);
        if (token == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }        ApiClient.getInstance(this).getService().getParkingLots(token).enqueue(new Callback<List<ParkingLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<ParkingLocation>> call, @NonNull Response<List<ParkingLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ParkingLocation> allParkingList = response.body();
                    List<ParkingLocation> nearbyParkingList = new ArrayList<>();
                    
                    for (ParkingLocation parking : allParkingList) {
                        double distance = calculateDistance(targetLocation.latitude, targetLocation.longitude,
                                                         parking.getLatitude(), parking.getLongitude());
                        if (distance <= 3) { // Within 3 km
                            nearbyParkingList.add(parking);
                        }
                    }
                    
                    updateMapWithParking(nearbyParkingList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkingLocation>> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
                Toast.makeText(DashboardActivity.this, "Failed to fetch parking locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                searchedLocation = new LatLng(address.getLatitude(), address.getLongitude());
                Log.d(TAG, "Search Location: " + searchedLocation.latitude + ", " + searchedLocation.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 16));
                fetchParkingLocations(searchedLocation);
            } else {
                Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error searching location: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error searching location", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMapWithParking(List<ParkingLocation> parkingList) {
        if (mMap == null || parkingList == null) return;

        mMap.clear();

        for (ParkingLocation parking : parkingList) {
            LatLng parkingLatLng = new LatLng(parking.getLatitude(), parking.getLongitude());
            float markerColor = BitmapDescriptorFactory.HUE_BLUE;
            
            if (parking.getTotalSlots() > 0) {
                double availabilityPercentage = (double) parking.getAvailableCarSlots() / parking.getTotalSlots() * 100;
                markerColor = getMarkerColor(availabilityPercentage);
            }

            MarkerOptions markerOptions = new MarkerOptions()
                .position(parkingLatLng)
                .title(parking.getParkingName())
                .snippet(String.format("Available: %d/%d\n%s",
                    parking.getAvailableCarSlots(),
                    parking.getTotalSlots(),
                    parking.getAddress()))
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor));
            
            mMap.addMarker(markerOptions);
        }
    }

    private float getMarkerColor(double availabilityPercentage) {
        if (availabilityPercentage >= 50) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (availabilityPercentage >= 20) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
