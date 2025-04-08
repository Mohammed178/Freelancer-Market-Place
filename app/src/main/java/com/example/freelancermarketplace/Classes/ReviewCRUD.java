package com.example.freelancermarketplace.Classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewCRUD {
    private DatabaseReference databaseReference;

    public ReviewCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
    }

    public void saveReview(Review review) {
        databaseReference.child(review.getReviewId()).setValue(review);
    }

    public void getReview(String reviewId, final ValueEventListener listener) {
        databaseReference.child(reviewId).addListenerForSingleValueEvent(listener);
    }

    public void updateReview(String reviewId, Review updatedReview) {
        databaseReference.child(reviewId).setValue(updatedReview);
    }

    public void deleteReview(String reviewId) {
        databaseReference.child(reviewId).removeValue();
    }

    public void getAllReviews(final ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(listener);
    }
}
