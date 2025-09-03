package com.task.mci;

import com.task.mci.dao.CrudDao;
import com.task.mci.dao.MciDao;
import com.task.mci.dao.impl.CachingDao;
import com.task.mci.dao.impl.CapacityDao;
import com.task.mci.dao.impl.CargoItemJoinedDao;
import com.task.mci.dao.impl.CargoItemShipmentDao;
import com.task.mci.dao.impl.CargoItemTreeDao;
import com.task.mci.dao.impl.LocationDao;
import com.task.mci.dao.impl.ProductDao;
import com.task.mci.dao.impl.ShipmentStageJoinedDao;
import com.task.mci.dao.impl.TruckDao;
import com.task.mci.model.Capacity;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.CargoItemShipmentKey;
import com.task.mci.model.Location;
import com.task.mci.model.Product;
import com.task.mci.model.ShipmentStage;
import com.task.mci.model.Truck;
import com.task.mci.service.GenericService;
import com.task.mci.service.MciService;
import com.task.mci.service.impl.CargoItemService;
import com.task.mci.service.impl.CargoItemShipmentService;
import com.task.mci.service.impl.GenericServiceImpl;
import com.task.mci.service.impl.JavaMciService;
import com.task.mci.service.impl.ShipmentStageService;

public class AppContext {
    public final CrudDao<Location,Integer> locationDao = new LocationDao();
    public final CrudDao<Truck, Integer> truckDao = new TruckDao();
    public final CrudDao<Product, Integer> productDao = new ProductDao();
    public final CrudDao<Capacity, Integer> capacityDao = new CachingDao<>(new CapacityDao(), Capacity::id);
    public final CrudDao<CargoItem, Integer> cargoItemDao = new CargoItemJoinedDao();
    public final CrudDao<ShipmentStage, Integer> shipmentStageDao = new ShipmentStageJoinedDao();
    public final CrudDao<CargoItemShipment, CargoItemShipmentKey> cargoItemShipmentDao = new CargoItemShipmentDao();
    public final MciDao mciDao = new CargoItemTreeDao();

    public final GenericService<Location, Integer> locationService = new GenericServiceImpl<>(locationDao);
    public final GenericService<Truck, Integer> truckService = new GenericServiceImpl<>(truckDao);
    public final GenericService<Product, Integer> productService = new GenericServiceImpl<>(productDao);
    public final GenericService<Capacity, Integer> capacityService = new GenericServiceImpl<>(capacityDao);
    public final GenericService<CargoItem, Integer> cargoItemService =
        new CargoItemService(cargoItemDao, capacityService, productService, locationService);
    public final GenericService<ShipmentStage, Integer> shipmentStageService =
        new ShipmentStageService(shipmentStageDao, truckService, locationService);
    public final GenericService<CargoItemShipment, CargoItemShipmentKey> cargoItemShipmentService =
        new CargoItemShipmentService(cargoItemShipmentDao, cargoItemService, shipmentStageService);
    public final MciService mciService = new JavaMciService(mciDao);
}
