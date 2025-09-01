// src/main/java/com/task/mci/dao/TruckDao.java
package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.Truck;

public class TruckDao implements CrudDao<Truck, Integer> {

    private static final String SELECT_ALL_SQL = "SELECT id, plate FROM trucks";
    private static final String SELECT_BY_ID_SQL = "SELECT id, plate FROM trucks WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO trucks(plate) VALUES(?)";

    @Override
    public List<Truck> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<Truck> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Truck(
                    rs.getInt("id"),
                    rs.getString("plate")
                ));
            }
            return list;
        }
    }

    @Override
    public Truck findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Truck(
                        rs.getInt("id"),
                        rs.getString("plate")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public Truck insert(Truck entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.plate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Truck(
                        rs.getInt(1),
                        entity.plate()
                    );
                }
                throw new SQLException("Can't get generated key for Truck");
            }
        }
    }
}
