package com.task.mci.service.validation;

import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Location;
import com.task.mci.service.exception.ValidationException;

public class LocationValidator implements Validator<Location> {
    @Override
    public void validate(Location loc) {
        List<String> errors = new ArrayList<>();
        if (loc.name() == null || loc.name().isBlank()) {
            errors.add("Location name must not be empty");
        }
        if (loc.name() != null && loc.name().length() > 255) {
            errors.add("Location name must be at most 255 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
