package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class Chore {
    private int id;
    private final UUID family;
    private final UUID submittedBy;
    private String submitterName;
    private UUID doneBy;
    private String doneByName;
    private String status;
    private final Date submissionDate;
    private Date deadline;
    private final String typeName;
    private final int typeId;
    private final String description;
    private final String title;

    public int getId() {
        return id;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getDoneByName() {
        return doneByName;
    }

    public void setDoneByName(String doneByName) {
        this.doneByName = doneByName;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Chore(UUID family, UUID submittedBy, Date submissionDate, Date deadline, String typeName, int typeId, String description, String title) {
        this.family = family;
        this.submittedBy = submittedBy;
        this.submissionDate = submissionDate;
        this.deadline = deadline;
        this.typeName = typeName;
        this.typeId = typeId;
        this.description = description;
        this.title = title;
    }
}
