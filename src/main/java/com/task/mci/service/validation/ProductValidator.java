package com.task.mci.service.validation;

import java.util.ArrayList;
import java.util.List;

import com.task.mci.model.Product;

public class ProductValidator implements Validator<Product> {
    @Override
    public void validate(Product prod) {
        List<String> errors = new ArrayList<>();
        if (prod.name() == null || prod.name().isBlank()) {
            errors.add("Product name must not be empty");
        }
        if (prod.name() != null && prod.name().length() > 100) {
            errors.add("Product name must be at most 100 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
