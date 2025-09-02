package com.task.mci.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.CrudDao;
import com.task.mci.dao.util.DB;
import com.task.mci.model.Location;

public class LocationDao implements CrudDao<Location, Integer> {

    private static final String SELECT_ALL_SQL = "SELECT id, name FROM locations";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name FROM locations WHERE id = ?";
    private static final String INSERT_SQL ="INSERT INTO locations(name) VALUES(?)";

    @Override
    public List<Location> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<Location> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Location(rs.getInt("id"), rs.getString("name")));
            }
            return list;
        }
    }

    @Override
    public Location findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Location(rs.getInt("id"), rs.getString("name"));
                }
                return null;
            }
        }
    }

    @Override
    public Location insert(Location entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Location(rs.getInt(1), entity.name());
                }
                throw new SQLException("Can't get generated key for Location");
            }
        }
    }
}
