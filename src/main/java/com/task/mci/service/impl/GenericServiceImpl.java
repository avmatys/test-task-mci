package com.task.mci.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.task.mci.dao.CrudDao;
import com.task.mci.service.GenericService;

public class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    private final CrudDao<T, ID> dao;

    public GenericServiceImpl(CrudDao<T, ID> dao) {
        this.dao = dao;
    }

    @Override
    public T create(T entity) throws SQLException {
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
