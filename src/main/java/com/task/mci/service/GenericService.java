package com.task.mci.service;

import java.sql.SQLException;
import java.util.List;

public interface GenericService<T, ID> {
    T create(T entity) throws SQLException;         
    T findById(ID id) throws SQLException;         
    List<T> findAll() throws SQLException; 
}