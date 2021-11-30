package com.lagosa.HomeManager.model;

public class ChoreType {
    private final int id;
    private final String type;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ChoreType(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
