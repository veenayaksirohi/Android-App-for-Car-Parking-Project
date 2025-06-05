package com.example.carparkingapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("user_email")
    private String user_email;
    
    @SerializedName("user_password")
    private String user_password;

    public LoginRequest(String email, String password) {
        this.user_email = email;
        this.user_password = password;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }
}
