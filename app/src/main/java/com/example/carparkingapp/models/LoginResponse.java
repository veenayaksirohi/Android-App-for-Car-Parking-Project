package com.example.carparkingapp.models;

public class LoginResponse {
    private String message;
    private String token;
    private int user_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    // Check if the response indicates success based on both token and message
    public boolean isSuccess() {
        if (message != null && message.toLowerCase().contains("failed")) {
            return false;
        }
        return token != null && !token.isEmpty();
    }
}
