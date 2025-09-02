package com.task.mci.model;

public record Capacity(int id, String name) {
    public Capacity(int id) {
        this(id, null);   
    }
}