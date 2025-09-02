package com.task.mci.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.dao.MciDao;
import com.task.mci.dao.util.DB;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;

public class CargoItemTreeDao implements MciDao {

    private static final String SELECT_ALL_BY_STAGE_ID = """
         WITH RECURSIVE subtree AS (
            SELECT ci.id, ci.type, ci.parent_id, ci.from_id, ci.to_id
            FROM cargo_items ci
            JOIN cargo_item_shipments cis ON ci.id = cis.cargo_item_id
            WHERE cis.shipment_stage_id = ? AND ci.parent_id IS NULL
            UNION ALL
            SELECT child.id, child.type,child.parent_id, child.from_id, child.to_id
            FROM cargo_items child
            JOIN subtree p ON p.id = child.parent_id
        )
        SELECT id, parent_id, type, from_id, to_id FROM subtree
    """;

    @Override
    public List<CargoItem> getCargoItemTree(int shipmentStageId) throws SQLException {
        List<CargoItem> result = new ArrayList<>();
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_BY_STAGE_ID)) {
            ps.setInt(1, shipmentStageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CargoItem item = new CargoItem( 
                        rs.getInt("id"), 
                        CargoType.valueOf(rs.getString("type")),
                        null, null, 
                        new Location(rs.getInt("from_id")),
                        new Location(rs.getInt("to_id")),
                        rs.getObject("parent_id") != null ? new CargoItem(rs.getInt("parent_id")) : null,
                        false
                    );
                    result.add(item);
                }
            }
        }
        return result;
    }
}
