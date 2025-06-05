package com.example.carparkingapp.models;

import java.util.List;

public class Floor {
    private int floorNumber;
    private List<ParkingSpot> spots;
    private int availableSpots;

    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSpot> getSpots() { return spots; }
    public int getAvailableSpots() { return availableSpots; }
}

class ParkingSpot {
    private String spotId;
    private boolean isOccupied;
    private String vehicleNumber;

    public String getSpotId() { return spotId; }
    public boolean isOccupied() { return isOccupied; }
    public String getVehicleNumber() { return vehicleNumber; }
}
