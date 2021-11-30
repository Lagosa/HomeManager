package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class Chore {
    private int id;
    private final UUID family;
    private final UUID submittedBy;
    private UUID doneBy;
    private String status;
    private final Date submissionDate;
    private Date deadline;
    private final String typeName;
    private final int typeId;
    private final String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoneBy(UUID doneBy) {
        this.doneBy = doneBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public UUID getFamily() {
        return family;
    }

    public UUID getSubmittedBy() {
        return submittedBy;
    }

    public UUID getDoneBy() {
        return doneBy;
    }

    public String getStatus() {
        return status;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getDescription() {
        return description;
    }

    public Chore(UUID family, UUID submittedBy, Date submissionDate, Date deadline, String typeName, int typeId, String description) {
        this.family = family;
        this.submittedBy = submittedBy;
        this.submissionDate = submissionDate;
        this.deadline = deadline;
        this.typeName = typeName;
        this.typeId = typeId;
        this.description = description;
    }
}
