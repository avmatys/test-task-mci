package com.task.mci.model;
 
public record CargoItemShipmentKey(
    int cargoItemId,
    int shipmentStageId
) {}