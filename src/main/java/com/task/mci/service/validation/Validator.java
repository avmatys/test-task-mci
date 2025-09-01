package com.task.mci.service.validation;

public interface Validator<T> {
    void validate(T entity, ValidationContext context);
}