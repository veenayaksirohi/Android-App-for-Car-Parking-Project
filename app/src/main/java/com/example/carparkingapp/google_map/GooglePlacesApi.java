package com.example.carparkingapp.google_map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApi {
    @GET("place/nearbysearch/json")
    Call<PlacesResponse> getNearbyParking(
            @Query("location") String location, // latitude,longitude
            @Query("radius") int radius,       // Radius in meters
            @Query("type") String type,        // Type of place
            @Query("key") String apiKey        // Google API Key
    );
}
