package com.example.carparkingapp.core.config;

public class ApiConfig {
    public static final String BASE_URL = "http://13.203.97.51:5000/";
    public static final String GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/maps/api/";
    
    // Network configurations
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    
    private ApiConfig() {
        // Private constructor to prevent instantiation
    }
}
