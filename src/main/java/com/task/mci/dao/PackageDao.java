package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.Package;

public class PackageDao implements CrudDao<Package, Integer> {

    public PackageDao() {
    }

    private static final String SELECT_ALL_SQL = "SELECT id, name FROM packages";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name FROM packages WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO packages(name) VALUES(?)";

    @Override
    public List<Package> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<Package> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Package(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return list;
        }
    }

    @Override
    public Package findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Package(
                        rs.getInt("id"),
                        rs.getString("name")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public Package insert(Package entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Package(
                        rs.getInt(1),
                        entity.name()
                    );
                }
                throw new SQLException("Can't get generated key for Package");
            }
        }
    }
}
