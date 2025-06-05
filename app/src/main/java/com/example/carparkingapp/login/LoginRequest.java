package com.example.carparkingapp.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("user_email")
    private final String userEmail;
    @SerializedName("user_password")
    private final String userPassword;

    public LoginRequest(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
