package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class Memento {
    private final UUID family;
    private final String title;
    private final Date dueDate;
    private ChoreStatus status;
    private int id;

    public Memento(UUID family, String title, Date dueDate) {
        this.family = family;
        this.title = title;
        this.dueDate = dueDate;
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

    public String getTitle() {
        return title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public ChoreStatus getStatus() {
        return status;
    }

    public void setStatus(ChoreStatus status) {
        this.status = status;
    }
}
