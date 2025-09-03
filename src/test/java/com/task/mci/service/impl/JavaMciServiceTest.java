package com.task.mci.service.impl;

import com.task.mci.dao.MciDao;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JavaMciServiceTest {

    @Test
    void testFindMciReturnsCorrectItems() throws SQLException {
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
