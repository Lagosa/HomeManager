package com.lagosa.HomeManager.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Poll {
    public Poll(UUID family, String message) {
        this.family = family;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getFamily() {
        return family;
    }

    public List<Map<String,Object>> getDishes() {
        return dishes;
    }

    public void setDishes(List<Map<String,Object>> dishes) {
        this.dishes = dishes;
    }

    public String getMessage() {
        return message;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
        this.status = status;
    }

    private int id;
    private final UUID family;
    private List<Map<String,Object>> dishes;
    private final String message;
    private PollStatus status;
}
