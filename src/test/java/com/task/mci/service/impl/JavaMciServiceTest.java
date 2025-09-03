package com.task.mci.service.impl;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.mci.dao.MciDao;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;

class JavaMciServiceTest {


    @Test
    void testFindMciReturnsCorrectItems1() throws SQLException {
        // Prepare
        MciDao mockDao = mock(MciDao.class);
        JavaMciService service = new JavaMciService(mockDao);
        Location locA = new Location(1, "A");
        Location locB = new Location(2, "B");
        Location locC = new Location(3, "C");
        Location locD = new Location(4, "D");

        // Hierarchy 1
        /*
        100 C 1-4
            101 P 1-3
            102 C 1-3
                106 C 1-3
                    108 C 1-3
                        110 C 1-3
                    109 C 1-3
                107 C 1-3
                    111 P 1-3
        */
        CargoItem root = new CargoItem(100, CargoType.CAPACITY, null, null, locA, locD, null, false);
        CargoItem child1 = new CargoItem(101, CargoType.PRODUCT, null, null, locA, locC, root, false);
        CargoItem child2 = new CargoItem(102, CargoType.CAPACITY, null, null, locA, locC, root, false);
        CargoItem child21 = new CargoItem(106, CargoType.CAPACITY, null, null, locA, locC, child2, false);
        CargoItem child22 = new CargoItem(107, CargoType.CAPACITY, null, null, locA, locC, child2, false);
        CargoItem child211 = new CargoItem(108, CargoType.CAPACITY, null, null, locA, locC, child21, false);
        CargoItem child2111 = new CargoItem(110, CargoType.CAPACITY, null, null, locA, locC, child211, false);
        CargoItem child212 = new CargoItem(109, CargoType.CAPACITY, null, null, locA, locC, child21, false);
        CargoItem child213 = new CargoItem(111, CargoType.PRODUCT, null, null, locA, locC, child21, false);

        // Herarchy 2
        // 103 C 1-2
        //  104 P 1-2
        //  105 P 1-2
        CargoItem root2 = new CargoItem(103, CargoType.CAPACITY, null, null, locA, locB, null, false);
        CargoItem childr1 = new CargoItem(104, CargoType.PRODUCT, null, null, locA, locB, root2, false);
        CargoItem childr2 = new CargoItem(105, CargoType.PRODUCT, null, null, locA, locB, root2, false);

        List<CargoItem> tree = List.of(root, child1, child2, child21, child22, child211, child2111, child212,
                                       child213, root2, childr1, childr2);
        when(mockDao.getCargoItemTree(1)).thenReturn(tree);
        // Run 
        List<CargoItem> result = service.findMci(1);
        // Check 
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(item -> item.id() == child1.id()));
        assertTrue(result.stream().anyMatch(item -> item.id() == child2.id()));
        assertTrue(result.stream().anyMatch(item -> item.id() == root2.id()));
    }

    @Test
    void testFindMciReturnsCorrectItems2() throws SQLException {
        // Prepare
        MciDao mockDao = mock(MciDao.class);
        JavaMciService service = new JavaMciService(mockDao);
        Location locA = new Location(1, "A");
        Location locB = new Location(2, "B");
        CargoItem root = new CargoItem(100, CargoType.CAPACITY, null, null, locA, locB, null, false);
        CargoItem child1 = new CargoItem(101, CargoType.PRODUCT, null, null, locA, locB, root, false);
        CargoItem child2 = new CargoItem(102, CargoType.PRODUCT, null, null, locA, locB, root, false);
        List<CargoItem> tree = List.of(root, child1, child2);
        when(mockDao.getCargoItemTree(1)).thenReturn(tree);
        // Run 
        List<CargoItem> result = service.findMci(1);
        // Check 
        assertEquals(1, result.size());
        assertEquals(root.id(), result.get(0).id());
    }

    @Test
    void testFindMciReturnsEmptyListWhenNoItems() throws SQLException {
        // Prepare
        MciDao mockDao = mock(MciDao.class);
        JavaMciService service = new JavaMciService(mockDao);
        when(mockDao.getCargoItemTree(2)).thenReturn(List.of());
        // Run
        List<CargoItem> result = service.findMci(2);
        // Check 
        assertTrue(result.isEmpty());
    }
}
