package com.example.freelancermarketplace.Classes;

public class Proposal {
    private String proposalId;
    private String jobId; // reference to the job the proposal is for
    private String freelancerId;
    private double bidAmount;
    private String proposalMessage;
    private long timestamp;
    private String status;

    public Proposal(String proposalId, String jobId, String freelancerId, double bidAmount, String proposalMessage, long timestamp, String status) {
        this.proposalId = proposalId;
        this.jobId = jobId;
        this.freelancerId = freelancerId;
        this.bidAmount = bidAmount;
        this.proposalMessage = proposalMessage;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Proposal() {
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(String freelancerId) {
        this.freelancerId = freelancerId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getProposalMessage() {
        return proposalMessage;
    }

    public void setProposalMessage(String proposalMessage) {
        this.proposalMessage = proposalMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
