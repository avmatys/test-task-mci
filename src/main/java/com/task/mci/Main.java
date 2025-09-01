package com.task.mci;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.task.mci.command.Command;
import com.task.mci.command.CommandFactory;
import com.task.mci.command.CommandRegistry;
import com.task.mci.dao.CachingDao;
import com.task.mci.dao.CapacityDao;
import com.task.mci.dao.CargoItemDao;
import com.task.mci.dao.CargoItemShipmentDao;
import com.task.mci.dao.CrudDao;
import com.task.mci.dao.LocationDao;
import com.task.mci.dao.ProductDao;
import com.task.mci.dao.ShipmentStageDao;
import com.task.mci.dao.TruckDao;
import com.task.mci.dao.util.DB;
import com.task.mci.io.ConsoleInputSource;
import com.task.mci.io.ConsoleOutputTarget;
import com.task.mci.io.FileInputSource;
import com.task.mci.io.FileOutputTarget;
import com.task.mci.io.InputSource;
import com.task.mci.io.OutputTarget;
import com.task.mci.model.Capacity;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.CargoItemShipmentKey;
import com.task.mci.model.Location;
import com.task.mci.model.Product;
import com.task.mci.model.ShipmentStage;
import com.task.mci.model.Truck;
import com.task.mci.service.GenericService;
import com.task.mci.service.impl.CargoItemService;
import com.task.mci.service.impl.CargoItemShipmentService;
import com.task.mci.service.impl.GenericServiceImpl;
import com.task.mci.service.impl.ShipmentStageService;

public class Main {
    public static void main(String[] args) {
        DB.init();
        InputSource in = null;
        OutputTarget out = null;
        try {
            if (args.length >= 4 && args[0].equalsIgnoreCase("-fin") && args[2].equalsIgnoreCase("-fout")) {
                in = new FileInputSource(args[1]);
                out = new FileOutputTarget(args[3]);
            } else {
                in = new ConsoleInputSource();
                out = new ConsoleOutputTarget();
            }
            
            // Initialize DAOs
            CrudDao<Location,Integer> locationDao = new LocationDao();
            CrudDao<Truck, Integer> truckDao = new TruckDao();
            CrudDao<Product, Integer> productDao = new ProductDao();
            CrudDao<Capacity, Integer> packageDao = new CachingDao<>(new CapacityDao(), Capacity::id);
            CrudDao<CargoItem, Integer> cargoItemDao = new CargoItemDao();
            CrudDao<ShipmentStage, Integer> shipmentStageDao = new ShipmentStageDao();
            CrudDao<CargoItemShipment, CargoItemShipmentKey> cargoItemShipmentDao = new CargoItemShipmentDao();
            
            // Initialize Services
            GenericService<Location, Integer> locationService = new GenericServiceImpl<>(locationDao);
            GenericService<Truck, Integer> truckService = new GenericServiceImpl<>(truckDao);
            GenericService<Product, Integer> productService = new GenericServiceImpl<>(productDao);
            GenericService<Capacity, Integer> capacityService = new GenericServiceImpl<>(packageDao);
            GenericService<CargoItem, Integer> cargoItemService = 
                new CargoItemService(cargoItemDao, capacityService, productService, locationService);
            GenericService<ShipmentStage, Integer> shipmentStageService = 
                new ShipmentStageService(shipmentStageDao, truckService, locationService);
            GenericService<CargoItemShipment, CargoItemShipmentKey> cargoItemShipmentService = 
                new CargoItemShipmentService(cargoItemShipmentDao, cargoItemService, shipmentStageService);
            
            // Register commands
            CommandRegistry registry = new CommandRegistry();
            registry.register("help", CommandFactory.helpCommand(registry));
            registry.register("exit", CommandFactory.exitCommand());
            
            registry.register("lst-loc", CommandFactory.listLocationCommand(locationService));
            registry.register("lst-trk", CommandFactory.listTruckCommand(truckService));
            registry.register("lst-pkg", CommandFactory.listCapacityCommand(capacityService));
            registry.register("lst-prd", CommandFactory.listProductCommand(productService));
            registry.register("lst-itm", CommandFactory.listCargoItemCommand(cargoItemService));
            registry.register("lst-stg", CommandFactory.listShipmentStageCommand(shipmentStageService));
            registry.register("lst-map", CommandFactory.listCargoItemShipmentCommand(cargoItemShipmentService));

            registry.register("add-loc", CommandFactory.addLocationCommand(locationService));
            registry.register("add-trk", CommandFactory.addTruckCommand(truckService));
            registry.register("add-pkg", CommandFactory.addCapacityCommand(capacityService));
            registry.register("add-prd", CommandFactory.addProductCommand(productService));
            registry.register("add-itm", CommandFactory.addCargoItemCommand(cargoItemService));
            registry.register("add-stg", CommandFactory.addShipmentStageCommand(shipmentStageService));
            registry.register("add-map", CommandFactory.addCargoItemShipmentCommand(cargoItemShipmentService));

            // Command loop
            Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
            boolean running = true;
            while (running) {
                out.write("\n> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                Matcher matcher = pattern.matcher(line);
                List<String> parts = new ArrayList<>();
                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        parts.add(matcher.group(1));
                    } else if (matcher.group(2) != null) {
                        parts.add(matcher.group(2));
                    } else {
                        parts.add(matcher.group(0));
                    }
                }
                if (parts.isEmpty()) continue;

                String cmdName = parts.get(0);
                String[] cmdArgs = parts.size() > 1 ? parts.subList(1, parts.size()).toArray(String[]::new) : new String[0];
                Command cmd = registry.get(cmdName);
                if (cmd == null) {
                    out.write("Unknown command\n");
                    continue;
                }
                running = cmd.execute(cmdArgs, in, out);
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
            DB.shutdown();
        }
    }
}