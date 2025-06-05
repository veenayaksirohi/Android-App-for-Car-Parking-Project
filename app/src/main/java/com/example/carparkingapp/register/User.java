package com.example.carparkingapp.register;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("user_password")
    private String userPassword;

    @SerializedName("user_phone_no")
    private long userPhoneNo;

    @SerializedName("user_address")
    private String userAddress;

    public User(String userName, String userEmail, String userPassword, long userPhoneNo, String userAddress) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNo = userPhoneNo;
        this.userAddress = userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public long getUserPhoneNo() {
        return userPhoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhoneNo(long userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoneNo=" + userPhoneNo +
                ", userAddress='" + userAddress + '\'' +
                '}';
    }
}
