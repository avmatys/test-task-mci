package com.task.mci.model;

public record CargoItemShipment(
    CargoItem cargoItem,
    ShipmentStage shipmentStage
) {}