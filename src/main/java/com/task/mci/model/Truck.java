package com.task.mci.model;

public record Truck(int id, String plate) {
    public Truck(int id) {
        this(id, null);
    }
}