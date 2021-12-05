package com.lagosa.HomeManager.model;

public class Ingredient {

    public Ingredient(String name, String measurementUnit) {
        this.name = name;
        this.measurementUnit = measurementUnit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private final String name;
    private final String measurementUnit;
    private String quantity;
}
