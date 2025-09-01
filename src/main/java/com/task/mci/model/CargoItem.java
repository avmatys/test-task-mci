package com.task.mci.model;

public record CargoItem(
    int id,
    CargoType type,
    Package pkg,
    Product product,
    Location from,
    Location to,
    CargoItem parent,
    boolean main
) {}