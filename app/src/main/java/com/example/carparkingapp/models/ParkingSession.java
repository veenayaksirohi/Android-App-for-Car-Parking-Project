package com.example.carparkingapp.models;

import java.math.BigDecimal;
import java.util.Date;

public class ParkingSession {
    private String ticket_id;
    private int parkinglot_id;
    private int floor_id;
    private int row_id;
    private int slot_id;
    private String vehicle_reg_no;
    private int user_id;
    private Date start_time;
    private Date end_time;
    private BigDecimal duration_hrs;

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(int parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
    }

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public String getVehicle_reg_no() {
        return vehicle_reg_no;
    }

    public void setVehicle_reg_no(String vehicle_reg_no) {
        this.vehicle_reg_no = vehicle_reg_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public BigDecimal getDuration_hrs() {
        return duration_hrs;
    }

    public void setDuration_hrs(BigDecimal duration_hrs) {
        this.duration_hrs = duration_hrs;
    }
} 