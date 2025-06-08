package com.example.carparkingapp.models;

public class RemoveCarRequest {
    private String ticket_id;

    public RemoveCarRequest(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }
} 