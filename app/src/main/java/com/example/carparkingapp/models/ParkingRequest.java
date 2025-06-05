package com.example.carparkingapp.models;

public class ParkingRequest {
    private String vehicleNumber;
    private int parkingLotId;
    private int floorNumber;
    private String spotId;

    public ParkingRequest(String vehicleNumber, int parkingLotId, int floorNumber, String spotId) {
        this.vehicleNumber = vehicleNumber;
        this.parkingLotId = parkingLotId;
        this.floorNumber = floorNumber;
        this.spotId = spotId;
    }

    // Getters
    public String getVehicleNumber() { return vehicleNumber; }
    public int getParkingLotId() { return parkingLotId; }
    public int getFloorNumber() { return floorNumber; }
    public String getSpotId() { return spotId; }
}
