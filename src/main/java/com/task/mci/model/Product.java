package com.task.mci.model;

public record Product(int id, String name) {
    public Product(int id) {
        this(id, null);
    }
}