package com.example.carparkingapp.utils;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private static final String TAG = "EnvConfig";
    private static Properties properties;
    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (isInitialized) {
            return;
        }

        properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open(".env");
            properties.load(inputStream);
            inputStream.close();
            validateRequiredProperties();
            isInitialized = true;
        } catch (IOException e) {
            Log.e(TAG, "Error loading .env file", e);
            throw new RuntimeException("Failed to load .env file. Please ensure it exists in assets folder", e);
        }
    }

    private static void validateRequiredProperties() {
        String[] requiredProps = {
            "BASE_URL",
            "API_VERSION",
            "GOOGLE_MAPS_API_KEY",
            "GOOGLE_MAPS_BASE_URL"
        };

        for (String prop : requiredProps) {
            if (!properties.containsKey(prop) || properties.getProperty(prop).isEmpty()) {
                throw new RuntimeException("Required property " + prop + " not found in .env file");
            }
        }
    }

    // API Keys
    public static String getGoogleMapsApiKey() {
        checkInitialization();
        return properties.getProperty("GOOGLE_MAPS_API_KEY");
    }

    // Server Configuration
    public static String getBaseUrl() {
        checkInitialization();
        return properties.getProperty("BASE_URL");
    }

    public static String getApiVersion() {
        checkInitialization();
        return properties.getProperty("API_VERSION");
    }

    public static int getApiTimeout() {
        checkInitialization();
        return Integer.parseInt(properties.getProperty("API_TIMEOUT", "30"));
    }

    // Google Maps Configuration
    public static String getGoogleMapsBaseUrl() {
        checkInitialization();
        return properties.getProperty("GOOGLE_MAPS_BASE_URL");
    }

    public static int getGoogleMapsDefaultZoom() {
        checkInitialization();
        return Integer.parseInt(properties.getProperty("GOOGLE_MAPS_DEFAULT_ZOOM", "15"));
    }

    public static double getGoogleMapsDefaultLat() {
        checkInitialization();
        return Double.parseDouble(properties.getProperty("GOOGLE_MAPS_DEFAULT_LAT", "0.0"));
    }

    public static double getGoogleMapsDefaultLng() {
        checkInitialization();
        return Double.parseDouble(properties.getProperty("GOOGLE_MAPS_DEFAULT_LNG", "0.0"));
    }

    // Network Configuration
    public static int getNetworkTimeout() {
        checkInitialization();
        return Integer.parseInt(properties.getProperty("NETWORK_TIMEOUT", "30"));
    }

    public static int getMaxRetryAttempts() {
        checkInitialization();
        return Integer.parseInt(properties.getProperty("MAX_RETRY_ATTEMPTS", "3"));
    }

    private static void checkInitialization() {
        if (!isInitialized) {
            throw new IllegalStateException("EnvConfig is not initialized. Call init() first.");
        }
    }
}