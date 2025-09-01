package com.task.mci.service.impl;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Package;
import com.task.mci.service.validation.PackageValidator;

public class PackageService extends GenericServiceImpl<Package, Integer> {
    public PackageService(CrudDao<Package, Integer> dao) {
        super(dao, new PackageValidator());

    }
}