package com.task.mci.service.validation;

import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Truck;

public class TruckValidator implements Validator<Truck> {
    @Override
    public void validate(Truck truck) {
        List<String> errors = new ArrayList<>();
        if (truck.plate() == null || truck.plate().isBlank()) {
            errors.add("Truck plate must not be empty");
        }
        if (truck.plate() != null && truck.plate().length() > 20) {
            errors.add("Truck plate must be at most 20 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
