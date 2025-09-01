package com.task.mci.service.validation;

import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Package;

public class PackageValidator implements Validator<Package> {
    @Override
    public void validate(Package pkg) {
        List<String> errors = new ArrayList<>();
        if (pkg.name() == null || pkg.name().isBlank()) {
            errors.add("Package name must not be empty");
        }
        if (pkg.name() != null && pkg.name().length() > 100) {
            errors.add("Package name must be at most 100 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
