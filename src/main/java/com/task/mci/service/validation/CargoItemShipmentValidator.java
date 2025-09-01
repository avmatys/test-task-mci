package com.task.mci.service.validation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.ShipmentStage;
import com.task.mci.service.GenericService;

public class CargoItemShipmentValidator 
    implements Validator<CargoItemShipment> {

    private final GenericService<CargoItem, Integer> itemSrv;
    private final GenericService<ShipmentStage, Integer> stageSrv;

    public CargoItemShipmentValidator(
        GenericService<CargoItem, Integer> itemSrv,
        GenericService<ShipmentStage, Integer> stageSrv
    ) {
        this.itemSrv = itemSrv;
        this.stageSrv = stageSrv;
    }

    @Override
    public void validate(CargoItemShipment cis, ValidationContext context) {
        List<String> errors = new ArrayList<>();
        if (context != ValidationContext.INSERT) return;
        try {
            CargoItem item = cis.cargoItem();
            if (item == null || itemSrv.findById(item.id()) == null) {
                errors.add( "Cargo item not found, id=" + (item != null ? item.id() : "null"));
            }
            ShipmentStage stage = cis.shipmentStage();
            if (stage == null || stageSrv.findById(stage.id()) == null) {
                errors.add("Shipment stage not found, id=" + (stage != null ? stage.id() : "null"));
            }
        } catch (SQLException e) {
            errors.add("SQL exception happened during validation: " + e.getMessage());
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
