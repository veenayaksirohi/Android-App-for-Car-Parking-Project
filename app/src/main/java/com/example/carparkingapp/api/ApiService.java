package com.example.carparkingapp.api;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<JsonObject> login(@Body JsonObject request);
    
    @POST("register")
    Call<JsonObject> register(@Body JsonObject request);
    
    @GET("parkinglots_details")
    Call<JsonObject> getParkingLots(@Header("Authorization") String token);
    
    @POST("park_car")
    Call<JsonObject> parkCar(@Header("Authorization") String token, @Body JsonObject request);
}
