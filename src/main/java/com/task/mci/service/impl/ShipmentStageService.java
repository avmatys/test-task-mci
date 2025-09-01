package com.task.mci.service.impl;

import java.sql.SQLException;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Location;
import com.task.mci.model.ShipmentStage;
import com.task.mci.model.Truck;
import com.task.mci.service.GenericService;
import com.task.mci.service.validation.ShipmentStageValidator;
import com.task.mci.service.validation.ValidationContext;
import com.task.mci.service.validation.Validator;

public class ShipmentStageService extends GenericServiceImpl<ShipmentStage,Integer> {

    private final Validator<ShipmentStage> validator;

    public ShipmentStageService(
        CrudDao<ShipmentStage,Integer> dao,
        GenericService<Truck,Integer> truckSrv,
        GenericService<Location,Integer> locactionSrv
    ) {
        super(dao);
        this.validator = new ShipmentStageValidator(truckSrv, locactionSrv);
    }

    @Override
    public ShipmentStage create(ShipmentStage entity) throws SQLException{
        validator.validate(entity, ValidationContext.INSERT);
        return super.create(entity);
    }
    
}