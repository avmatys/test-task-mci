package com.task.mci.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T, ID> {
    List<T> findAll() throws SQLException;
    T findById(ID id) throws SQLException;
    T insert(T entity) throws SQLException;
}
