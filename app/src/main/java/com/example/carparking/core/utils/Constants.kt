package com.example.carparking.core.utils

object Constants {
    // Network
    const val BASE_URL = "https://api.carparking.com/"
    const val TIMEOUT_SECONDS = 30L
    
    // Database
    const val DATABASE_NAME = "car_parking_db"
    const val DATABASE_VERSION = 1
    
    // Shared Preferences
    const val PREF_NAME = "car_parking_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    
    // Validation
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_PASSWORD_LENGTH = 32
    const val PHONE_NUMBER_LENGTH = 10
} 