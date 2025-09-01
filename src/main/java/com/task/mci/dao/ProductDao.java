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
import com.task.mci.model.Product;

public class ProductDao implements CrudDao<Product, Integer> {

    private static final String SELECT_ALL_SQL = "SELECT id, name FROM products";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name FROM products WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO products(name) VALUES(?)";

    @Override
    public List<Product> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
            }
            return list;
        }
    }

    @Override
    public Product findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public Product insert(Product entity) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt(1),
                        entity.name()
                    );
                }
                throw new SQLException("Can't get generated key for Product");
            }
        }
    }
}
