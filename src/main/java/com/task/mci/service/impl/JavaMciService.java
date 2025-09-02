package com.task.mci.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.task.mci.dao.MciDao;
import com.task.mci.model.CargoItem;
import com.task.mci.service.MciService;

public class JavaMciService implements MciService {

    private final MciDao dao;

    public JavaMciService(MciDao dao) {
        this.dao = dao;
    }

    @Override
    public List<CargoItem> findMci(int shipmentStageId) throws SQLException {
        return dao.getCargoItemTree(shipmentStageId);
    }

}
