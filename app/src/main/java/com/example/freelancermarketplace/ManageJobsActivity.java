package com.example.freelancermarketplace;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobCRUD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageJobsActivity extends AppCompatActivity {

    private ListView jobListView;
    private List<Job> jobList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String clientId = "client123"; // Replace with actual logged-in client ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_jobs);

        jobListView = findViewById(R.id.jobs_list_view);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        jobListView.setAdapter(adapter);

        new JobCRUD().getAllJobs(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                adapter.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    if (job != null && job.getClientId().equals(clientId)) {
                        jobList.add(job);
                        adapter.add(job.getTitle() + " - " + job.getStatus());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageJobsActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
