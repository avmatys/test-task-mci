package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.CargoItemShipmentKey;
import com.task.mci.model.ShipmentStage;

public class CargoItemShipmentDao implements CrudDao<CargoItemShipment, CargoItemShipmentKey> {

    private static final String SELECT_ALL_SQL = """
        SELECT                    
          cis.cargo_item_id       AS ci_id,
          cis.shipment_stage_id   AS ss_id
        FROM cargo_items_shipments cis
        """;

    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL
        + " WHERE cis.cargo_item_id = ? AND cis.shipment_stage_id = ?";

    private static final String INSERT_SQL = """
        INSERT INTO cargo_items_shipments(cargo_item_id, shipment_stage_id)
        VALUES (?, ?)
        """;

    @Override
    public List<CargoItemShipment> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<CargoItemShipment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    @Override
    public CargoItemShipment findById(CargoItemShipmentKey key) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, key.cargoItemId());
            ps.setInt(2, key.shipmentStageId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public CargoItemShipment insert(CargoItemShipment entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.NO_GENERATED_KEYS)) {
            ps.setInt(1, entity.cargoItem().id());
            ps.setInt(2, entity.shipmentStage().id());
            ps.executeUpdate();
            return entity;
        }
    }

    private CargoItemShipment mapRow(ResultSet rs) throws SQLException {
        CargoItem item = new CargoItem(rs.getInt("ci_id"), null, null, null, null, null, null, false);
        ShipmentStage stage = new ShipmentStage(rs.getInt("ss_id"), null, null, null);
        return new CargoItemShipment(item, stage);
    }
}
