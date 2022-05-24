package com.deitel.pms.messaging;

public class Message {

    private String content;
    private String createdAt;
    private String senderId;
    private String recipient;

    public Message(String message, String date, String id, String recipient) {
        this.content = message;
        this.createdAt = date;
        this.senderId = id;
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
