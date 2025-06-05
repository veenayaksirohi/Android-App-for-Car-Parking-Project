package com.example.carparkingapp.google_map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientMap {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static GooglePlacesApi getApiService() {
        return retrofit.create(GooglePlacesApi.class);
    }
}
