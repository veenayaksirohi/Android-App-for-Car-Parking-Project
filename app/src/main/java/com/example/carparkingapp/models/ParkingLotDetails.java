package com.example.carparkingapp.models;

public class ParkingLotDetails {
    private int id;
    private String name;
    private String location;
    private int totalSpots;
    private int availableSpots;
    private double hourlyRate;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getTotalSpots() { return totalSpots; }
    public int getAvailableSpots() { return availableSpots; }
    public double getHourlyRate() { return hourlyRate; }
}
