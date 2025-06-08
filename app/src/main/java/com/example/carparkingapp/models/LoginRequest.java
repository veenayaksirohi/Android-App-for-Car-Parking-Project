package com.example.carparkingapp.models;

public class LoginRequest {
    private String user_email;
    private String user_password;
    private String user_phone_no;

    public LoginRequest(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public LoginRequest(String user_phone_no, String user_password, boolean isPhone) {
        this.user_phone_no = user_phone_no;
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone_no() {
        return user_phone_no;
    }

    public void setUser_phone_no(String user_phone_no) {
        this.user_phone_no = user_phone_no;
    }
}
