package com.task.mci.command;

import com.task.mci.command.templates.GenericAddCommand;
import com.task.mci.command.templates.GenericCommand;
import com.task.mci.command.templates.GenericListCommand;
import com.task.mci.command.templates.ParamSpec;
import com.task.mci.model.Capacity;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoItemShipment;
import com.task.mci.model.CargoItemShipmentKey;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;
import com.task.mci.model.Product;
import com.task.mci.model.ShipmentStage;
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

    public static Command listCapacityCommand(GenericService<Capacity, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.name(), "list all capacity types");
    }

    public static Command listCargoItemCommand(GenericService<CargoItem, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.type(), "list all cargo items");
    }

    public static Command listShipmentStageCommand(GenericService<ShipmentStage, Integer> service) {
        return new GenericListCommand<>(service, x -> x.id() + "\t" + x.truck(), "list all shipment stages");
    }

    public static Command listCargoItemShipmentCommand(GenericService<CargoItemShipment, CargoItemShipmentKey> service) {
        return new GenericListCommand<>(service, x -> x.cargoItem().id() + "\t" + x.shipmentStage().id(), "list all item shipments");
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
            }
        );
    }   

    public static Command exitCommand() {
        return new GenericCommand("exit the application", (args, in, out) -> false);
    }

    public static Command addLocationCommand(GenericService<Location, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter location name", true)
        };
        return new GenericAddCommand<>(
            srv,
            specs,
            params -> new Location(0, params.get("-name")),
            created -> "Location added: ID=" + created.id() + ", name=" + created.name(),
            "add a new location. Usage: add-location [-i] -name <name>"
        );
    }

    public static Command addTruckCommand(GenericService<Truck, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-plate", "Enter truck plate", true)
        };
        return new GenericAddCommand<>(
            srv, 
            specs,
            params -> new Truck(0, params.get("-plate")),
            created -> "Truck added: ID=" + created.id() + ", plate=" + created.plate(),
            "add a new truck. Usage: add-trk [-i] -plate <plate>"
        );
    }

    public static Command addCapacityCommand(GenericService<Capacity, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec("-name", "Enter name", true)
        };
        return new GenericAddCommand<>(
            srv, 
            specs,
            params -> new Capacity(0, params.get("-name")),
            created -> "Added: ID=" + created.id() + ", name=" + created.name(), 
            "add a new capacity type. Usage: add-cpt [-i] -name <name>"
        );
    }   

    public static Command addProductCommand(GenericService<Product, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] { 
            new ParamSpec("-name", "Enter name", true) 
        };
        return new GenericAddCommand<>(
            srv, 
            specs,
            params -> new Product(0, params.get("-name")),
            created -> "Added: ID=" + created.id() + ", name=" + created.name(),
            "add a new product. Usage: add-prd [-i] -name <name>"
        );
    }  

    public static Command addCargoItemCommand(GenericService<CargoItem, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec(
                "-type", 
                "Enter cargo type (PRODUCT, CAPACITY)", 
                map -> true,
                val -> val.equals("PRODUCT") || val.equals("CAPACITY"),
                "type must be PRODUCT or CAPACITY"
            ),
            new ParamSpec(
                "-capId", 
                "Enter capacity ID for type CAPACITY", 
                map -> "CAPACITY".equalsIgnoreCase(map.get("-type")),
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "capacity ID is required for type CAPACITY"
            ),
            new ParamSpec(
                "-prdId",
                "Enter product ID for type PRODUCT", 
                map -> "PRODUCT".equalsIgnoreCase(map.get("-type")),
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "product ID is required for type PRODUCT"
            ),
            new ParamSpec(
                "-fromId",
                "Enter source location ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "source location ID must be a valid integer"
            ),
            new ParamSpec(
                "-toId",
                "Enter target location ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "target location ID must be a valid integer"
            ),
            new ParamSpec(
                "-parentId",
                "Enter parent cargo item ID (optional)",
                map -> false,
                val -> {
                    if (val == null || val.isBlank()) return true;
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "parent cargo item ID must be a valid integer"
            )
        };
        return new GenericAddCommand<>(
            srv, 
            specs,
            params -> {
                CargoType type = CargoType.valueOf(params.get("-type"));
                CargoItem parent = null;
                if (params.containsKey("-parentId") && !params.get("-parentId").isBlank()) {
                    parent = new CargoItem(Integer.parseInt(params.get("-parentId")), null, null, null, null, null, null, false);
                }
                return new CargoItem(
                    0,
                    type,
                    type == CargoType.CAPACITY ? new Capacity(Integer.parseInt(params.get("-capId")), "") : null,
                    type == CargoType.PRODUCT ? new Product(Integer.parseInt(params.get("-prdId")), "") : null,
                    new Location(Integer.parseInt(params.get("-fromId")), ""),
                    new Location(Integer.parseInt(params.get("-toId")), ""),
                    parent, 
                    false
                );
            },
            created -> "Added: ID=" + created.id() + ", parent=" + (created.parent() != null ? created.parent().id() : "null"),
            "add a new cargo item. Usage: add-itm [-i] -type <CAPACITY|PRODUCT> [-capId <id>] [-prdId <id>] -fromId <id> -toId <id> [-parentId <id>]"
        );
    }

    public static Command addShipmentStageCommand(GenericService<ShipmentStage, Integer> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec(
                "-truckId",
                "Enter truck ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "truck ID must be a valid integer"
            ),
            new ParamSpec(
                "-fromId",
                "Enter source location ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "fromId must be a valid integer"
            ),
            new ParamSpec(
                "-toId",
                "Enter target location ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "toId must be a valid integer"
            )
        };

        return new GenericAddCommand<>(
            srv,
            specs,
            params -> {
                return new ShipmentStage(
                    0,
                    new Truck(Integer.parseInt(params.get("-truckId")), ""),
                    new Location(Integer.parseInt(params.get("-fromId")),  ""),
                    new Location(Integer.parseInt(params.get("-toId")),    "")
                );
            },
            created -> String.format(
                "ShipmentStage added: ID=%d, truck=%d, from=%d, to=%d",
                created.id(),
                created.truck().id(),
                created.from().id(),
                created.to().id()
            ),
            "add-shipment-stage [-i] -truckId <id> -fromId <id> -toId <id>"
        );
    }

    public static Command addCargoItemShipmentCommand(GenericService<CargoItemShipment, CargoItemShipmentKey> srv) {
        ParamSpec[] specs = new ParamSpec[] {
            new ParamSpec(
                "-itemId",
                "Enter item ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "item ID must be a valid integer"
            ),
            new ParamSpec(
                "-stageId",
                "Enter shipment stage ID",
                map -> true,
                val -> {
                    try {
                        Integer.valueOf(val);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "stageId must be a valid integer"
            )
        };

        return new GenericAddCommand<>(
            srv,
            specs,
            params -> {
                return new CargoItemShipment(
                    new CargoItem(Integer.parseInt(params.get("-itemId")), null, null, null, null, null, null, false),
                    new ShipmentStage(Integer.parseInt(params.get("-stageId")), null, null, null)
                );
            },
            created -> "Added: cargo item=" + created.cargoItem().id() + ", shipment stage=" + created.shipmentStage().id(),
            "lnk-itm [-i] -itemId <id> -stageId <id>"
        );
    }

}