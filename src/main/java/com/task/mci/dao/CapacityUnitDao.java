package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.CapacityUnit;

public class CapacityUnitDao implements CrudDao<CapacityUnit, Integer> {

    private static final String SELECT_ALL_SQL = "SELECT id, name, sequence FROM capacity_units";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name, sequence FROM capacity_units WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO capacity_units(name, sequence) VALUES(?, ?)";

    @Override
    public List<CapacityUnit> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<CapacityUnit> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new CapacityUnit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("sequence")
                ));
            }
            return list;
        }
    }

    @Override
    public CapacityUnit findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CapacityUnit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("sequence")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public CapacityUnit insert(CapacityUnit entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.name());
            ps.setInt(2, entity.sequence());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new CapacityUnit(
                        rs.getInt(1),
                        entity.name(),
                        entity.sequence()
                    );
                }
                throw new SQLException("Can't get generated key for CapacityUnit");
            }
        }
    }
}
