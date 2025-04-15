package com.example.freelancermarketplace.Classes;

public class Review {
    private String reviewId;
    private String jobId; // reference to the job the review is for
    private String clientId; // client
    private String freelancerId;
    private int rating; // 1 to 5 stars
    private String comments;
    private long timestamp;

    public Review(String reviewId, String jobId, String reviewerId, String revieweeId, int rating, String comments, long timestamp) {
        this.reviewId = reviewId;
        this.jobId = jobId;
        this.clientId = reviewerId;
        this.freelancerId = revieweeId;
        this.rating = rating;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    public Review() {
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
