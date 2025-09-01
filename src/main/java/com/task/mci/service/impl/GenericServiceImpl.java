package com.task.mci.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.task.mci.dao.CrudDao;
import com.task.mci.service.GenericService;
import com.task.mci.service.validation.Validator;

public class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    private final CrudDao<T, ID> dao;
    private final Validator<T> validator;

    public GenericServiceImpl(CrudDao<T, ID> dao, Validator<T> validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public T create(T entity) throws SQLException {
        validator.validate(entity);
        return dao.insert(entity);
    }

    @Override
    public T findById(ID id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public List<T> findAll() throws SQLException {
        return dao.findAll();
    }

}
