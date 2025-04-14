package com.example.freelancermarketplace;


import java.io.Serializable;

public class Message implements Serializable {
    private String messageId;
    private String senderId;
    private String proposalId;
    private String receiverId;
    private String text;
    private long timestamp;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String messageId, String proposalId, String senderId, String receiverId, String messageText, long timestamp) {
        this.messageId = messageId;
        this.proposalId = proposalId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = messageText;
        this.timestamp = timestamp;
    }


    // Getters and setters

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
