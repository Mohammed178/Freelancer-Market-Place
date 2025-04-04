package com.example.freelancermarketplace.Classes;

public class Job {
    private String jobId;
    private String title;
    private String description;
    private double budget;
    private String status; // "open", "in progress", "completed"
    private String clientId; // reference to the client who posted the job
    private String freelancerId; // reference to the freelancer hired (null if not hired yet)
    private long timestamp;

    public Job(String jobId, String title, String description, double budget, String status, String clientId, String freelancerId, long timestamp) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.status = status;
        this.clientId = clientId;
        this.freelancerId = freelancerId;
        this.timestamp = timestamp;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(String freelancerId) {
        this.freelancerId = freelancerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
