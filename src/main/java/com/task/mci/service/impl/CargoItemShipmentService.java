package com.task.mci.service.impl;

import java.sql.SQLException;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.CargoItemShipmentKey;
import com.task.mci.model.ShipmentStage;
import com.task.mci.service.GenericService;
import com.task.mci.service.validation.CargoItemShipmentValidator;
import com.task.mci.service.validation.ValidationContext;
import com.task.mci.service.validation.Validator;

public class CargoItemShipmentService extends GenericServiceImpl<CargoItemShipment,CargoItemShipmentKey> {

    private final Validator<CargoItemShipment> validator;

    public CargoItemShipmentService(
        CrudDao<CargoItemShipment,CargoItemShipmentKey> dao,
        GenericService<CargoItem,Integer> itemSrv,
        GenericService<ShipmentStage,Integer> stageSrv
    ) {
        super(dao);
        this.validator = new CargoItemShipmentValidator(itemSrv, stageSrv);
    }

    @Override
    public CargoItemShipment create(CargoItemShipment entity) throws SQLException{
        validator.validate(entity, ValidationContext.INSERT);
        return super.create(entity);
    }
    
}