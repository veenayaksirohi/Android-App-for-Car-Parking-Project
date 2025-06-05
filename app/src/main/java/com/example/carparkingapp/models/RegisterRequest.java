package com.example.carparkingapp.models;

public class RegisterRequest {
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_phone_no;
    private String user_address;

    public RegisterRequest(String name, String email, String password, String phone, String address) {
        this.user_name = name;
        this.user_email = email;
        this.user_password = password;
        this.user_phone_no = phone;
        this.user_address = address;
    }

    // Getters
    public String getUser_name() { return user_name; }
    public String getUser_email() { return user_email; }
    public String getUser_password() { return user_password; }
    public String getUser_phone_no() { return user_phone_no; }
    public String getUser_address() { return user_address; }
}
