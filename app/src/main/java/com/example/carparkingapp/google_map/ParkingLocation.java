package com.example.carparkingapp.google_map;

public class ParkingLocation {
    private int parkingId;
    private String parkingName;
    private String city;
    private String parkingLocation;
    private String address1;
    private String address2;
    private double latitude;
    private double longitude;
    private int totalSlots;
    private int availableSlots;

    // Getters
    public int getParkingId() { return parkingId; }
    public String getParkingName() { return parkingName; }
    public String getCity() { return city; }
    public String getParkingLocation() { return parkingLocation; }
    public String getAddress1() { return address1; }
    public String getAddress2() { return address2; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getTotalSlots() { return totalSlots; }
    public int getAvailableSlots() { return availableSlots; }
    
    // Additional getter methods needed by the UI
    public String getAddress() {
        String address = address1;
        if (address2 != null && !address2.isEmpty()) {
            address += ", " + address2;
        }
        if (city != null && !city.isEmpty()) {
            address += ", " + city;
        }
        return address;
    }
    
    public int getAvailableCarSlots() {
        return availableSlots;
    }

    // Setters
    public void setParkingId(int parkingId) { this.parkingId = parkingId; }
    public void setParkingName(String parkingName) { this.parkingName = parkingName; }
    public void setCity(String city) { this.city = city; }
    public void setParkingLocation(String parkingLocation) { this.parkingLocation = parkingLocation; }
    public void setAddress1(String address1) { this.address1 = address1; }
    public void setAddress2(String address2) { this.address2 = address2; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }
}
