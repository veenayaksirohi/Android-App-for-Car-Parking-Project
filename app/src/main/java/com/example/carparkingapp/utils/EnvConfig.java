package com.example.carparkingapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class EnvConfig {
    private static final String TAG = "EnvConfig";
    private static Properties properties;

    public static void init(Context context) {
        properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Error loading config.properties", e);
        }
    }

    public static String getGoogleMapsApiKey() {
        return properties.getProperty("GOOGLE_MAPS_API_KEY", "");
    }
} 