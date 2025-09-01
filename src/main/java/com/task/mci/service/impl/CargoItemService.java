package com.task.mci.service.impl;

import java.sql.SQLException;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Capacity;
import com.task.mci.model.CargoItem;
import com.task.mci.model.Location;
import com.task.mci.model.Product;
import com.task.mci.service.GenericService;
import com.task.mci.service.validation.CargoItemValidator;
import com.task.mci.service.validation.ValidationContext;
import com.task.mci.service.validation.Validator;

public class CargoItemService extends GenericServiceImpl<CargoItem,Integer> {

    private final Validator<CargoItem> validator;

    public CargoItemService(
        CrudDao<CargoItem,Integer> dao,
        GenericService<Capacity,Integer> capacitySrv,
        GenericService<Product,Integer> productSrv,
        GenericService<Location,Integer> locactionSrv
    ) {
        super(dao);
        this.validator = new CargoItemValidator(capacitySrv, productSrv, locactionSrv, this);
    }

    @Override
    public CargoItem create(CargoItem entity) throws SQLException{
        validator.validate(entity, ValidationContext.INSERT);
        return super.create(entity);
    }
    
}