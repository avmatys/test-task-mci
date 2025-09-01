package com.task.mci.service.validation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Location;
import com.task.mci.model.ShipmentStage;
import com.task.mci.model.Truck;
import com.task.mci.service.GenericService;

public class ShipmentStageValidator implements Validator<ShipmentStage > {

    private final GenericService<Truck, Integer> truckSrv;
    private final GenericService<Location, Integer> locationSrv;

    public ShipmentStageValidator(
        GenericService<Truck, Integer> truckSrv,
        GenericService<Location, Integer> locationSrv
    ) {
        this.truckSrv = truckSrv;
        this.locationSrv = locationSrv;
    }

    @Override
    public void validate(ShipmentStage stage, ValidationContext context) {
        List<String> errors = new ArrayList<>();
        if (context != ValidationContext.INSERT) return;
        try {
            Truck truck = stage.truck();
            if (truck == null || truckSrv.findById(truck.id()) == null) {
                errors.add("Truck not found, id=" + (truck != null ? truck.id() : "null"));
            }
            Location from = stage.from();
            Location to = stage.to();
            if (from == null || locationSrv.findById(from.id()) == null) {
                errors.add("Source location not found, id=" + (from != null ? from.id() : "null"));
            }
            if (to == null || locationSrv.findById(to.id()) == null) {
                errors.add("Target location not found, id=" + (to != null ? stage.to().id() : "null"));
            }
        } catch (SQLException e) {
            errors.add("SQL exception happened during validation: " + e.getMessage());
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

