package com.task.mci.service.impl;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.task.mci.dao.MciDao;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.service.MciService;

public class JavaMciService implements MciService {

    private final MciDao dao;

    public JavaMciService(MciDao dao) {
        this.dao = dao;
    }

    @Override
    public List<CargoItem> findMci(int shipmentStageId) throws SQLException {
        List<CargoItem> items = dao.getCargoItemTree(shipmentStageId);
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, List<CargoItem>> tree = new HashMap<>();
        for (CargoItem item : items) {
            Integer parentId = item.parent() != null ? item.parent().id() : null;
            tree.computeIfAbsent(parentId, k -> new java.util.ArrayList<>()).add(item);
        }
        Map<Integer, NodeInfo> stats = new HashMap<>();
        for (CargoItem item : items) {
            this.computeNodeInfo(item, tree, stats);
        }
        List<CargoItem> result = new ArrayList<>();
        Deque<CargoItem> queue = new ArrayDeque<>();
        queue.addAll(tree.get(null));
        while (!queue.isEmpty()) {
            CargoItem curr = queue.removeFirst();
            NodeInfo currInfo = stats.get(curr.id());
            if (currInfo.hasProduct && currInfo.sameLocation) {
                result.add(curr);
            } else {
                queue.addAll(tree.getOrDefault(curr.id(), Collections.emptyList()));
            }
        }

        return result;
    }

    private NodeInfo computeNodeInfo(CargoItem item, Map<Integer, List<CargoItem>> tree, Map<Integer, NodeInfo> stats){
        if (stats.containsKey(item.id())) 
            return stats.get(item.id());
        boolean hasProduct = item.type() == CargoType.PRODUCT;
        boolean sameLocation = true;
        List<CargoItem> childs = tree.getOrDefault(item.id(), Collections.emptyList());
        for (CargoItem c: childs) {
            NodeInfo childInfo = computeNodeInfo(c, tree, stats);
            hasProduct |= childInfo.hasProduct;
            sameLocation &= childInfo.sameLocation 
                            && item.from().id() == c.from().id() 
                            && item.to().id() == c.to().id();
        }
        NodeInfo res = new NodeInfo(hasProduct, sameLocation);
        stats.put(item.id(), res);
        return res;

    }

    private static class NodeInfo {
        boolean hasProduct;
        boolean sameLocation;
        NodeInfo(boolean hasProduct, boolean sameLocation) {
            this.hasProduct = hasProduct;
            this.sameLocation = sameLocation;
        }
    }

}
