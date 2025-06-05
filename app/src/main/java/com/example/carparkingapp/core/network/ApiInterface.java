package com.example.carparkingapp.core.network;

import com.example.carparkingapp.models.ParkingLocation;
import com.example.carparkingapp.models.LoginRequest;
import com.example.carparkingapp.models.LoginResponse;
import com.example.carparkingapp.models.User;
import com.example.carparkingapp.models.RegisterRequest;
import com.example.carparkingapp.models.RegisterResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("/users")
    Call<User> postUser(@Body User user);

    @GET("/locations") 
    Call<List<ParkingLocation>> getParkingLocations();

    @GET("/parkinglots_details")
    Call<List<ParkingLocation>> getParkingLots(@Header("Authorization") String token);
}
