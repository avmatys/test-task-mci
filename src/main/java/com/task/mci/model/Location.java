package com.task.mci.model;

public record Location(int id, String name) {
    public Location(int id) {
        this(id, null);
    }
}