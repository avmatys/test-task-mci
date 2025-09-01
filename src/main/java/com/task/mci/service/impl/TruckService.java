package com.task.mci.service.impl;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Truck;
import com.task.mci.service.validation.TruckValidator;

public class TruckService extends GenericServiceImpl<Truck, Integer> {
    public TruckService(CrudDao<Truck, Integer> dao) {
        super(dao, new TruckValidator());
    }
}