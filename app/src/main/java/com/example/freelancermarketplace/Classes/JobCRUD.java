package com.example.freelancermarketplace.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobCRUD {
    private DatabaseReference databaseReference;

    public JobCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("jobs");
    }

    public void saveJob(Job job) {
        databaseReference.child(job.getJobId()).setValue(job);
    }

    public void getJob(String jobId, final ValueEventListener listener) {
        databaseReference.child(jobId).addListenerForSingleValueEvent(listener);
    }

    public void updateJob(String jobId, Job updatedJob) {
        databaseReference.child(jobId).setValue(updatedJob);
    }

    public void deleteJob(String jobId) {
        databaseReference.child(jobId).removeValue();
    }

    public void getAllJobs(final ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(listener);
    }
}
