package com.task.mci.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.util.DB;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;
import com.task.mci.model.Package;
import com.task.mci.model.Product;

public class CargoItemDao implements CrudDao<CargoItem, Integer> {

    private static final String SELECT_ALL_SQL = "SELECT "
            + " ci.id                 AS ci_id,"
            + " ci.cargo_type         AS ci_type,"
            + " ci.main_flag          AS ci_main,"
            + " ci.package_id         AS pkg_id,"
            + " p.name                AS pkg_name,"
            + " ci.product_id         AS prod_id,"
            + " pr.name               AS prod_name,"
            + " ci.from_id            AS from_id,"
            + " fl.name               AS from_name,"
            + " ci.to_id              AS to_id,"
            + " tl.name               AS to_name,"
            + " ci.parent_id          AS parent_id"
            + " FROM cargo_items ci"
            + " LEFT JOIN packages p  ON p.id  = ci.package_id"
            + " LEFT JOIN products pr ON pr.id = ci.product_id"
            + " LEFT JOIN locations fl ON fl.id = ci.from_id"
            + " LEFT JOIN locations tl ON tl.id = ci.to_id";

    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE ci.id = ?";

    private static final String INSERT_SQL = "INSERT INTO cargo_items("
            + "  cargo_type, package_id, product_id,"
            + "  main_flag, from_id, to_id, parent_id"
            + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<CargoItem> findAll() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            List<CargoItem> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    @Override
    public CargoItem findById(Integer id) throws SQLException {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public CargoItem insert(CargoItem entity) throws SQLException {
        try (Connection c = DB.getConnection();
            PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            // 1) cargo_type
            ps.setString(1, entity.type().name());
            // 2) package_id
            if (entity.pkg() != null) {
                ps.setInt(2, entity.pkg().id());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            // 3) product_id
            if (entity.product() != null) {
                ps.setInt(3, entity.product().id());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            // 4) main_flag
            ps.setBoolean(4, entity.main());
            // 5) from_id
            if (entity.from() != null) {
                ps.setInt(5, entity.from().id());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            // 6) to_id
            if (entity.to() != null) {
                ps.setInt(6, entity.to().id());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            // 7) parent_id
            if (entity.parent() != null) {
                ps.setInt(7, entity.parent().id());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new CargoItem(
                        newId, entity.type(), entity.pkg(), entity.product(),
                        entity.from(), entity.to(), entity.parent(), entity.main()
                    );
                } else {
                    throw new SQLException("Can't get generated key for CargoItem");
                }
            }
        }
    }


    private CargoItem mapRow(ResultSet rs) throws SQLException {
        int id       = rs.getInt("ci_id");
        CargoType type = CargoType.valueOf(rs.getString("ci_type"));
        boolean main  = rs.getBoolean("ci_main");
        // package
        Package pkg = null;
        int pkgId = rs.getInt("pkg_id");
        if (!rs.wasNull()) {
            pkg = new Package(pkgId, rs.getString("pkg_name"));
        }
        // product
        Product prod = null;
        int prodId = rs.getInt("prod_id");
        if (!rs.wasNull()) {
            prod = new Product(prodId, rs.getString("prod_name"));
        }
        // from location
        Location from = null;
        int fromId = rs.getInt("from_id");
        if (!rs.wasNull()) {
            from = new Location(fromId, rs.getString("from_name"));
        }
        // to location
        Location to = null;
        int toId = rs.getInt("to_id");
        if (!rs.wasNull()) {
            to = new Location(toId, rs.getString("to_name"));
        }
        // parent (ID‐only)
        CargoItem parent = null;
        int parentId = rs.getInt("parent_id");
        if (!rs.wasNull()) {
            parent = new CargoItem(parentId, null, null, null, null, null, null, false);
        }
        return new CargoItem(id, type, pkg, prod, from, to, parent, main);
    }
}
