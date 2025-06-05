package com.example.carparkingapp.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("user_id")
    private int userId;

    // Default constructor (needed for Gson/Retrofit)
    public LoginResponse() { }

    // Getters for the fields
    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    // Check if the response indicates success based on both token and message
    public boolean isSuccess() {
        if (message != null && message.toLowerCase().contains("failed")) {
            return false;
        }
        return token != null && !token.isEmpty();
    }
}

