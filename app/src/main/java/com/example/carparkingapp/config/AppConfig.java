package com.example.carparkingapp.config;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig {
    private static final String PREF_NAME = "app_config";
    private static final String KEY_API_BASE_URL = "api_base_url";
    private static AppConfig instance;
    private final SharedPreferences preferences;
    private static final String DEFAULT_API_BASE_URL = "http://192.168.84.191:5000/";

    private AppConfig(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new AppConfig(context);
        }
    }

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            initialize(context);
        }
        return instance;
    }

    public String getApiBaseUrl() {
        return preferences.getString(KEY_API_BASE_URL, DEFAULT_API_BASE_URL);
    }

    public void setApiBaseUrl(String baseUrl) {
        preferences.edit().putString(KEY_API_BASE_URL, baseUrl).apply();
    }
} 