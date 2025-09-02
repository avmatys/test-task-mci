package com.task.mci.model;

public record ShipmentStage(int id, Truck truck, Location from, Location to) {
    public ShipmentStage(int id) {
        this(id, null, null, null);
    }
}