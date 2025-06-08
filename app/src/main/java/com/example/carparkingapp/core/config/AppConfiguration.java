package com.example.carparkingapp.core.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.example.carparkingapp.R;
import com.example.carparkingapp.utils.EnvConfig;

/**
 * Unified configuration class for the application.
 * Combines API configuration, environment settings, and application constants.
 */
public class AppConfiguration {
    private static final String TAG = "AppConfiguration";
    
    private static AppConfiguration instance;
    private final Context context;

    // API Endpoints
    public static final String LOGIN_ENDPOINT = "/auth/login";
    public static final String REGISTER_ENDPOINT = "/auth/register";
    public static final String PARKING_SPOTS_ENDPOINT = "/parking/spots";
    public static final String BOOK_PARKING_ENDPOINT = "/parking/book";
    
    // API Headers
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    // API Response Codes
    public static final int RESPONSE_SUCCESS = 200;
    public static final int RESPONSE_CREATED = 201;
    public static final int RESPONSE_BAD_REQUEST = 400;
    public static final int RESPONSE_UNAUTHORIZED = 401;
    public static final int RESPONSE_FORBIDDEN = 403;
    public static final int RESPONSE_NOT_FOUND = 404;
    public static final int RESPONSE_SERVER_ERROR = 500;

    private Integer connectionTimeout;
    private Integer readTimeout;
    private String baseUrl;
    private String apiVersion;
    private String googleMapsBaseUrl;

    private AppConfiguration(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void initialize(Application application) {
        if (instance == null) {
            instance = new AppConfiguration(application);
            instance.loadConfiguration();
            logConfigLoaded();
        }
    }

    public static AppConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppConfiguration must be initialized with application context first");
        }
        return instance;
    }

    private void loadConfiguration() {
        // Load timeouts from EnvConfig with fallback to default values
        connectionTimeout = EnvConfig.getNetworkTimeout();
        readTimeout = EnvConfig.getNetworkTimeout();

        // Load URLs from EnvConfig with fallback to resource strings
        baseUrl = EnvConfig.getBaseUrl();
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = context.getString(R.string.api_base_url);
        }

        apiVersion = EnvConfig.getApiVersion();
        googleMapsBaseUrl = EnvConfig.getGoogleMapsBaseUrl();
    }

    // API Configuration
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getGoogleMapsBaseUrl() {
        return googleMapsBaseUrl;
    }

    public int getConnectionTimeout() {
        return connectionTimeout != null ? connectionTimeout : 30;
    }

    public int getReadTimeout() {
        return readTimeout != null ? readTimeout : 30;
    }

    public String getMapsApiKey() {
        return context.getString(R.string.maps_api_key);
    }

    public boolean isDebug() {
        return context.getResources().getBoolean(R.bool.debug);
    }

    // Helper methods
    public String getFullUrl(String endpoint) {
        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }
        return baseUrl + endpoint;
    }

    public static String getAuthHeader(String token) {
        return "Bearer " + token;
    }
    
    public static void logApiError(String endpoint, int statusCode, String message) {
        Log.e(TAG, String.format("API Error - Endpoint: %s, Status: %d, Message: %s", 
            endpoint, statusCode, message));
    }

    private static void logConfigLoaded() {
        Log.d(TAG, "Configuration loaded successfully");
        Log.d(TAG, "Using API URL: " + instance.getBaseUrl());
        Log.d(TAG, "Debug mode: " + instance.isDebug());
    }
}
