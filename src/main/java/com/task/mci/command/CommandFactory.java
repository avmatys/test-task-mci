package com.task.mci.command;

import com.task.mci.command.templates.GenericAddCommand;
import com.task.mci.command.templates.GenericCommand;
import com.task.mci.command.templates.GenericListCommand;
import com.task.mci.command.templates.ParamSpec;
import com.task.mci.dao.CrudDao;
import com.task.mci.model.CapacityUnit;
import com.task.mci.model.Location;
import com.task.mci.model.Truck;

public class CommandFactory {

    public static Command listLocationCommand(CrudDao<Location, Integer> locationDao) {
        return new GenericListCommand<>(locationDao, loc -> loc.id() + "\t" + loc.name(), "list all locations");
    }

    public static Command listTruckCommand(CrudDao<Truck, Integer> truckDao) {
        return new GenericListCommand<>(truckDao, trk -> trk.id() + "\t" + trk.plate(), "list all trucks");
    }

    public static Command listCapacityUnitCommand(CrudDao<CapacityUnit, Integer> capacityUnitDao) {
        return new GenericListCommand<>(capacityUnitDao, cu -> cu.id() + "\t" + cu.name() + "\t" + cu.sequence(), "list all capacity units");
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

    public static Command addLocationCommand(CrudDao<Location, Integer> locationDao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter location name", true)
        };
        return new GenericAddCommand<>(
            locationDao,
            specs,
            params -> new Location(0, params.get("-name")),
            created -> "Location added: ID=" + created.id() +
                    ", name=" + created.name(),
            "add a new location. Usage: add-location [-i] -name <name>");
    }

    public static Command addTruckCommand(CrudDao<Truck, Integer> truckDao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-plate", "Enter truck plate", true)
        };
        return new GenericAddCommand<>(
            truckDao,
            specs,
            params -> new Truck(0, params.get("-plate")),
            created -> "Truck added: ID=" + created.id() +
                    ", plate=" + created.plate(),
            "add a new truck. Usage: add-truck [-i] -plate <plate>");
    }

    public static Command addCapacityUnitCommand(CrudDao<CapacityUnit, Integer> capacityUnitDao) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter name", true),
            new ParamSpec("-seq",  "Enter sequence", true)
        };
        return new GenericAddCommand<>(
            capacityUnitDao,
            specs,
            params -> new CapacityUnit(0,
                params.get("-name"), Integer.parseInt(params.get("-seq"))),
            created -> "Added: ID=" + created.id()
                + ", name=" + created.name() + ", seq=" + created.sequence(),
            "add-capacity-unit [-i] -name <name> -seq <sequence>");
    }   
   
}