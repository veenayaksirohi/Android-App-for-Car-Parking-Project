package com.example.carparkingapp.models;

public class ParkingResponse {
    private boolean success;
    private String message;
    private String ticketNumber;
    private String parkingTime;

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getTicketNumber() { return ticketNumber; }
    public String getParkingTime() { return parkingTime; }
}
