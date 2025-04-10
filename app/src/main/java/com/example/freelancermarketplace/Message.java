package com.example.freelancermarketplace;

public class Message {
    private String content;
    private boolean isSent; // true for user messages, false for received
    private long timestamp;

    public Message(String content, boolean isSent) {
        this.content = content;
        this.isSent = isSent;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getContent() { return content; }
    public boolean isSent() { return isSent; }
    public long getTimestamp() { return timestamp; }
}
