package com.task.mci.dao;

import java.sql.SQLException;
import java.util.List;

import com.task.mci.model.CargoItem;

public interface MciDao {
    List<CargoItem> getCargoItemTree(int shipmentStageId) throws SQLException;
}