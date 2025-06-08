package com.example.carparkingapp.models;

public class ParkingLotDetails {
    private int parkinglot_id;
    private String parking_name;
    private String city;
    private String landmark;
    private String address;
    private double latitude;
    private double longitude;
    private String physical_appearance;
    private String parking_ownership;
    private String parking_surface;
    private String has_cctv;
    private String has_boom_barrier;
    private String ticket_generated;
    private String entry_exit_gates;
    private String weekly_off;
    private String parking_timing;
    private String vehicle_types;
    private int car_capacity;
    private int available_car_slots;
    private int two_wheeler_capacity;
    private int available_two_wheeler_slots;
    private String parking_type;
    private String payment_modes;
    private String car_parking_charge;
    private String two_wheeler_parking_charge;
    private String allows_prepaid_passes;
    private String provides_valet_services;
    private String value_added_services;

    public int getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(int parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
    }

    public String getParking_name() {
        return parking_name;
    }

    public void setParking_name(String parking_name) {
        this.parking_name = parking_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhysical_appearance() {
        return physical_appearance;
    }

    public void setPhysical_appearance(String physical_appearance) {
        this.physical_appearance = physical_appearance;
    }

    public String getParking_ownership() {
        return parking_ownership;
    }

    public void setParking_ownership(String parking_ownership) {
        this.parking_ownership = parking_ownership;
    }

    public String getParking_surface() {
        return parking_surface;
    }

    public void setParking_surface(String parking_surface) {
        this.parking_surface = parking_surface;
    }

    public String getHas_cctv() {
        return has_cctv;
    }

    public void setHas_cctv(String has_cctv) {
        this.has_cctv = has_cctv;
    }

    public String getHas_boom_barrier() {
        return has_boom_barrier;
    }

    public void setHas_boom_barrier(String has_boom_barrier) {
        this.has_boom_barrier = has_boom_barrier;
    }

    // Helper methods
    public boolean hasCCTV() {
        return "Y".equalsIgnoreCase(has_cctv);
    }

    public boolean hasBoomBarrier() {
        return "Y".equalsIgnoreCase(has_boom_barrier);
    }

    // Additional getters/setters following the same pattern
    public String getTicket_generated() {
        return ticket_generated;
    }

    public void setTicket_generated(String ticket_generated) {
        this.ticket_generated = ticket_generated;
    }

    public String getEntry_exit_gates() {
        return entry_exit_gates;
    }

    public void setEntry_exit_gates(String entry_exit_gates) {
        this.entry_exit_gates = entry_exit_gates;
    }

    public String getWeekly_off() {
        return weekly_off;
    }

    public void setWeekly_off(String weekly_off) {
        this.weekly_off = weekly_off;
    }

    public String getParking_timing() {
        return parking_timing;
    }

    public void setParking_timing(String parking_timing) {
        this.parking_timing = parking_timing;
    }

    public String getVehicle_types() {
        return vehicle_types;
    }

    public void setVehicle_types(String vehicle_types) {
        this.vehicle_types = vehicle_types;
    }

    public int getCar_capacity() {
        return car_capacity;
    }

    public void setCar_capacity(int car_capacity) {
        this.car_capacity = car_capacity;
    }

    public int getAvailable_car_slots() {
        return available_car_slots;
    }

    public void setAvailable_car_slots(int available_car_slots) {
        this.available_car_slots = available_car_slots;
    }

    public int getTwo_wheeler_capacity() {
        return two_wheeler_capacity;
    }

    public void setTwo_wheeler_capacity(int two_wheeler_capacity) {
        this.two_wheeler_capacity = two_wheeler_capacity;
    }

    public int getAvailable_two_wheeler_slots() {
        return available_two_wheeler_slots;
    }

    public void setAvailable_two_wheeler_slots(int available_two_wheeler_slots) {
        this.available_two_wheeler_slots = available_two_wheeler_slots;
    }

    public String getParking_type() {
        return parking_type;
    }

    public void setParking_type(String parking_type) {
        this.parking_type = parking_type;
    }

    public String getPayment_modes() {
        return payment_modes;
    }

    public void setPayment_modes(String payment_modes) {
        this.payment_modes = payment_modes;
    }

    public String getCar_parking_charge() {
        return car_parking_charge;
    }

    public void setCar_parking_charge(String car_parking_charge) {
        this.car_parking_charge = car_parking_charge;
    }

    public String getTwo_wheeler_parking_charge() {
        return two_wheeler_parking_charge;
    }

    public void setTwo_wheeler_parking_charge(String two_wheeler_parking_charge) {
        this.two_wheeler_parking_charge = two_wheeler_parking_charge;
    }

    public String getAllows_prepaid_passes() {
        return allows_prepaid_passes;
    }

    public void setAllows_prepaid_passes(String allows_prepaid_passes) {
        this.allows_prepaid_passes = allows_prepaid_passes;
    }

    public String getProvides_valet_services() {
        return provides_valet_services;
    }

    public void setProvides_valet_services(String provides_valet_services) {
        this.provides_valet_services = provides_valet_services;
    }

    public String getValue_added_services() {
        return value_added_services;
    }

    public void setValue_added_services(String value_added_services) {
        this.value_added_services = value_added_services;
    }

    // Utility methods for the UI
    public String getParkingName() {
        return parking_name;
    }

    // Utility methods for map display
    public int getAvailableSlots() {
        return available_car_slots + available_two_wheeler_slots;
    }

    public int getTotalSlots() {
        return car_capacity + two_wheeler_capacity;
    }

    public double getAvailabilityPercentage() {
        int total = getTotalSlots();
        if (total == 0) return 0;
        return (getAvailableSlots() * 100.0) / total;
    }

    public String getDisplayAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null && !address.isEmpty()) {
            sb.append(address);
        }
        if (landmark != null && !landmark.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Near ").append(landmark);
        }
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        return sb.toString();
    }

    public String getDisplayPrice() {
        if (car_parking_charge != null && !car_parking_charge.isEmpty()) {
            return "₹" + car_parking_charge + "/hr";
        }
        return "Price not available";
    }
}