package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class Notification {


    private int id;
    private final UUID sender;
    private String senderName;
    private final UUID receiver;
    private String receiverName;
    private final String title;
    private final String message;
    private Date dateSent;
    private String status;

    public Notification(UUID sender, UUID receiver, String title, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.message = message;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
