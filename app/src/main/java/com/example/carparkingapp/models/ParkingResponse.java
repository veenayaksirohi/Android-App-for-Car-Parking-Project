package com.example.carparkingapp.models;

public class ParkingResponse {
    private String message;
    private String ticket_id;
    private AssignedSlot assigned_slot;

    public static class AssignedSlot {
        private int floor_id;
        private int row_id;
        private int slot_id;

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
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public AssignedSlot getAssigned_slot() {
        return assigned_slot;
    }

    public void setAssigned_slot(AssignedSlot assigned_slot) {
        this.assigned_slot = assigned_slot;
    }
} 