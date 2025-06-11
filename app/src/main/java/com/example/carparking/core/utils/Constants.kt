package com.example.carparking.core.utils

object Constants {
    // Network
    const val BASE_URL = "http://10.0.2.2:5000/"
    const val TIMEOUT_SECONDS = 30L
    const val MAPS_API_KEY = "AIzaSyDDEUYgQL0bZoq3WqqDfXFBsjSSAU_6DH8"

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
