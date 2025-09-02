package com.task.mci.service;

import java.sql.SQLException;
import java.util.List;

import com.task.mci.model.CargoItem;

public interface MciService {
    List<CargoItem> findMci(int shipmentStageId) throws SQLException;
}   