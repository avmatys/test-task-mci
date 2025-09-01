package com.task.mci.service.validation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Capacity;
import com.task.mci.model.CargoItem;
import com.task.mci.model.CargoType;
import com.task.mci.model.Location;
import com.task.mci.model.Product;
import com.task.mci.service.GenericService;

public class CargoItemValidator implements Validator<CargoItem> {

    private final GenericService<Capacity, Integer> packageSrv;
    private final GenericService<Product, Integer> productSrv;
    private final GenericService<Location, Integer> locationSrv;
    private final GenericService<CargoItem, Integer> cargoSrv;

    public CargoItemValidator(
        GenericService<Capacity, Integer> packageSrv,
        GenericService<Product, Integer> productSrv,
        GenericService<Location, Integer> locationSrv,
        GenericService<CargoItem, Integer> cargoSrv
    ) {
        this.packageSrv = packageSrv;
        this.productSrv = productSrv;
        this.locationSrv = locationSrv;
        this.cargoSrv = cargoSrv;
    }

    @Override
    public void validate(CargoItem ci, ValidationContext context) {
        List<String> errors = new ArrayList<>();
        if (context != ValidationContext.INSERT) return;
        try {
            Capacity cap = ci.capacity();
            Product prd = ci.product();
            if (ci.type() == CargoType.CAPACITY && (cap == null || packageSrv.findById(cap.id()) == null)) {
                errors.add("Package not found, id=" + (cap != null ? cap.id() : "null"));
            }
            if (ci.type() == CargoType.PRODUCT && (prd == null || productSrv.findById(prd.id()) == null)) {
                errors.add("Product not found, id=" + (prd != null ? prd.id() : "null"));
            }
            Location from = ci.from();
            Location to = ci.to();
            if (from == null || locationSrv.findById(from.id()) == null) {
                errors.add("Source location not found, id=" + (from != null ? from.id() : "null"));
            }
            if (to == null || locationSrv.findById(to.id()) == null) {
                errors.add("Target location not found, id=" + (to != null ? ci.to().id() : "null"));
            }
            CargoItem parent = ci.parent();
            if (parent != null && cargoSrv.findById(parent.id()) == null) {
                errors.add("Parent CargoItem not found, id=" + parent.id());
            }
        } catch (SQLException e) {
            errors.add("SQL exception happened during validation: " + e.getMessage());
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

