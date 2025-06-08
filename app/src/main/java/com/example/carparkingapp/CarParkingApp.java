package com.example.carparkingapp;

import android.app.Application;
import com.example.carparkingapp.config.AppConfig;

public class CarParkingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig.initialize(this);
    }
}
