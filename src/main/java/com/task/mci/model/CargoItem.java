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
) {}