package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class Chore extends Memento{

    private final UUID submittedBy;
    private String submitterName;
    private UUID doneBy;
    private String doneByName;
    private final Date submissionDate;
    private final String typeName;
    private final int typeId;
    private final String description;

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

    public void setDoneBy(UUID doneBy) {
        this.doneBy = doneBy;
    }

    public UUID getSubmittedBy() {
        return submittedBy;
    }

    public UUID getDoneBy() {
        return doneBy;
    }

    public Date getSubmissionDate() {
        return submissionDate;
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

    public Chore(UUID family, UUID submittedBy, Date submissionDate, Date deadline, String typeName, int typeId, String description, String title) {
       super(family,title,deadline);
        this.submittedBy = submittedBy;
        this.submissionDate = submissionDate;
        this.typeName = typeName;
        this.typeId = typeId;
        this.description = description;
    }
}
