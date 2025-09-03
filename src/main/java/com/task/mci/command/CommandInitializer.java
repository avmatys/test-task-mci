package com.task.mci.command;

import com.task.mci.AppContext;

public class CommandInitializer {

    public static CommandRegistry initialize(AppContext ctx) {
        CommandRegistry registry = new CommandRegistry();

        registry.register("help", CommandFactory.helpCommand(registry));
        registry.register("exit", CommandFactory.exitCommand());

        registry.register("lst-loc", CommandFactory.listLocationCommand(ctx.locationService));
        registry.register("lst-trk", CommandFactory.listTruckCommand(ctx.truckService));
        registry.register("lst-pkg", CommandFactory.listCapacityCommand(ctx.capacityService));
        registry.register("lst-prd", CommandFactory.listProductCommand(ctx.productService));
        registry.register("lst-itm", CommandFactory.listCargoItemCommand(ctx.cargoItemService));
        registry.register("lst-stg", CommandFactory.listShipmentStageCommand(ctx.shipmentStageService));
        registry.register("lst-map", CommandFactory.listCargoItemShipmentCommand(ctx.cargoItemShipmentService));

        registry.register("add-loc", CommandFactory.addLocationCommand(ctx.locationService));
        registry.register("add-trk", CommandFactory.addTruckCommand(ctx.truckService));
        registry.register("add-pkg", CommandFactory.addCapacityCommand(ctx.capacityService));
        registry.register("add-prd", CommandFactory.addProductCommand(ctx.productService));
        registry.register("add-itm", CommandFactory.addCargoItemCommand(ctx.cargoItemService));
        registry.register("add-stg", CommandFactory.addShipmentStageCommand(ctx.shipmentStageService));
        registry.register("add-map", CommandFactory.addCargoItemShipmentCommand(ctx.cargoItemShipmentService));

        registry.register("lst-mci", CommandFactory.mciCommand(ctx.mciService));

        return registry;
    }

}
