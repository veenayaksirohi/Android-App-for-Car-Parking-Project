package com.example.carparkingapp.models;

public class ParkingRequest {
    private String parking_lot_name;
    private String vehicle_reg_no;
    private Integer floor_id;
    private Integer row_id;
    private Integer slot_id;

    // Constructors for flexibility
    public ParkingRequest() {}

    public ParkingRequest(String parking_lot_name, String vehicle_reg_no) {
        this.parking_lot_name = parking_lot_name;
        this.vehicle_reg_no = vehicle_reg_no;
    }

    public ParkingRequest(String parking_lot_name, String vehicle_reg_no, Integer floor_id, Integer row_id, Integer slot_id) {
        this.parking_lot_name = parking_lot_name;
        this.vehicle_reg_no = vehicle_reg_no;
        this.floor_id = floor_id;
        this.row_id = row_id;
        this.slot_id = slot_id;
    }

    // Getters and setters
    public String getParking_lot_name() {
        return parking_lot_name;
    }

    public void setParking_lot_name(String parking_lot_name) {
        this.parking_lot_name = parking_lot_name;
    }

    public String getVehicle_reg_no() {
        return vehicle_reg_no;
    }

    public void setVehicle_reg_no(String vehicle_reg_no) {
        this.vehicle_reg_no = vehicle_reg_no;
    }

    public Integer getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(Integer floor_id) {
        this.floor_id = floor_id;
    }

    public Integer getRow_id() {
        return row_id;
    }

    public void setRow_id(Integer row_id) {
        this.row_id = row_id;
    }

    public Integer getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(Integer slot_id) {
        this.slot_id = slot_id;
    }

    // Utility methods
    public boolean hasSpecificSpot() {
        return floor_id != null && row_id != null && slot_id != null;
    }
}