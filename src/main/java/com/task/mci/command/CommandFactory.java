package com.task.mci.command;

import com.task.mci.command.templates.GenericAddCommand;
import com.task.mci.command.templates.GenericCommand;
import com.task.mci.command.templates.GenericListCommand;
import com.task.mci.command.templates.ParamSpec;
import com.task.mci.dao.CrudDao;
import com.task.mci.model.Location;
import com.task.mci.model.Package;
import com.task.mci.model.Product;
import com.task.mci.model.Truck;
import com.task.mci.service.GenericService;

public class CommandFactory { 

    public static Command listLocationCommand(GenericService<Location, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.name(), "list all locations");
    }

    public static Command listTruckCommand(GenericService<Truck, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.plate(), "list all trucks");
    }

    public static Command listProductCommand(GenericService<Product, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.name(), "list all products");
    }

    public static Command listPackageCommand(GenericService<Package, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.name(), "list all package types");
    }

    public static Command helpCommand(CommandRegistry registry) {
        return new GenericCommand(
            "list all commands",
            (args, in, out) -> {
                out.write("Available commands:\n");
                for (var e : registry.all().entrySet()) {
                    out.write("  " + e.getKey() + " – " + e.getValue().description() + "\n");
                }
                return true; 
            });
    }   

    public static Command exitCommand() {
        return new GenericCommand(
            "exit the application",
            (args, in, out) -> false);
    }

    public static Command addLocationCommand(CrudDao<Location, Integer> dao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter location name", true)
        };
        return new GenericAddCommand<>(
            dao,
            specs,
            params -> new Location(0, params.get("-name")),
            created -> "Location added: ID=" + created.id() + ", name=" + created.name(),
            "add a new location. Usage: add-location [-i] -name <name>");
    }

    public static Command addTruckCommand(CrudDao<Truck, Integer> dao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-plate", "Enter truck plate", true)
        };
        return new GenericAddCommand<>(dao, specs,
            params -> new Truck(0, params.get("-plate")),
            created -> "Truck added: ID=" + created.id() + ", plate=" + created.plate(),
            "add a new truck. Usage: add-trk [-i] -plate <plate>");
    }

    public static Command addPackageCommand(CrudDao<Package, Integer> dao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter name", true),
            new ParamSpec("-seq",  "Enter sequence", true)
        };
        return new GenericAddCommand<>(dao, specs,
            params -> new Package(0, params.get("-name")),
            created -> "Added: ID=" + created.id() + ", name=" + created.name(), 
            "add a new package type. Usage: add-pkg [-i] -name <name>");
    }   

    public static Command addProductCommand(CrudDao<Product, Integer> dao) {
        ParamSpec[] specs = new ParamSpec[] { 
            new ParamSpec("-name", "Enter name", true) 
        };
        return new GenericAddCommand<>(dao, specs,
            params -> new Product(0, params.get("-name")),
            created -> "Added: ID=" + created.id() + ", name=" + created.name(),
            "add a new product. Usage: add-prd [-i] -name <name>");
    }   
   
}