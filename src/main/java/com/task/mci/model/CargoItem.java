package com.task.mci.model;

public record CargoItem(
    int id, 
    CargoType type,
    Capacity capacity,
    Product product,
    Location from,
    Location to,
    CargoItem parent,
    boolean main
) {
    public CargoItem(int id) {
        this(id, null, null, null, null, null, null, false);
    }
}