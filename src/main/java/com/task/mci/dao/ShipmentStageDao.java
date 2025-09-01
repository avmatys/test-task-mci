package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.Location;
import com.task.mci.model.ShipmentStage;
import com.task.mci.model.Truck;

public class ShipmentStageDao implements CrudDao<ShipmentStage, Integer> {

    private static final String SELECT_ALL_SQL = """
        SELECT ss.id         AS ss_id,
               ss.truck_id   AS truck_id,
               t.plate       AS truck_plate,
               ss.from_id    AS from_id,
               fl.name       AS from_name,
               ss.to_id      AS to_id,
               tl.name       AS to_name
          FROM shipment_stages ss
          JOIN trucks t     ON t.id  = ss.truck_id
          JOIN locations fl ON fl.id = ss.from_id
          JOIN locations tl ON tl.id = ss.to_id
        """;

    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE ss.id = ?";
    private static final String INSERT_SQL = """
        INSERT INTO shipment_stages(truck_id, from_id, to_id)
        VALUES (?, ?, ?)
        """;

    @Override
    public List<ShipmentStage> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<ShipmentStage> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    @Override
    public ShipmentStage findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public ShipmentStage insert(ShipmentStage entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.truck().id());
            ps.setInt(2, entity.from().id());
            ps.setInt(3, entity.to().id());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new ShipmentStage(newId, entity.truck(), entity.from(), entity.to());
                } else {
                    throw new SQLException("Can't get generated key for ShipmentStage");
                }
            }
        }
    }

    private ShipmentStage mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("ss_id");
        Truck truck = new Truck(rs.getInt("truck_id"), rs.getString("truck_plate"));
        Location from = new Location(rs.getInt("from_id"), rs.getString("from_name"));
        Location to   = new Location(rs.getInt("to_id"), rs.getString("to_name"));
        return new ShipmentStage(id, truck, from, to);
    }
}
