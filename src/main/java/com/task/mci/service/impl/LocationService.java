package com.task.mci.service.impl;  

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Location;
import com.task.mci.service.validation.LocationValidator;

public class LocationService extends GenericServiceImpl<Location, Integer> {
    public LocationService(CrudDao<Location, Integer> dao) {
        super(dao, new LocationValidator());
    }
}