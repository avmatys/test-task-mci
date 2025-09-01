package com.task.mci.service.impl;

import com.task.mci.dao.CrudDao;
import com.task.mci.model.Product;
import com.task.mci.service.validation.ProductValidator;

public class ProductService extends GenericServiceImpl<Product, Integer> {
    public ProductService(CrudDao<Product, Integer> dao) {
        super(dao, new ProductValidator());
    }
}