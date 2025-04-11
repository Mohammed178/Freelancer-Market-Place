package com.example.freelancermarketplace;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobAdapter;
import com.example.freelancermarketplace.Classes.JobCRUD;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BrowseJobsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.example.freelancermarketplace.JobAdapterBrowse jobAdapter;
    private List<Job> jobList = new ArrayList<>();
    private List<Job> filteredList = new ArrayList<>();
    private JobCRUD jobCRUD = new JobCRUD();
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_jobs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Adds a back button
        getSupportActionBar().setTitle("Add New Job");  // Set title

        currentUserId = getIntent().getStringExtra("userID");

        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new com.example.freelancermarketplace.JobAdapterBrowse(filteredList,currentUserId);
        recyclerView.setAdapter(jobAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterJobs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterJobs(newText);
                return true;
            }
        });

        loadJobs();
    }

    private void loadJobs() {
        jobCRUD.getAllJobs(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                filteredList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    if (job != null && "open".equalsIgnoreCase(job.getStatus())) {
                        jobList.add(job);
                        filteredList.add(job);
                    }
                }
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BrowseJobsActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterJobs(String keyword) {
        filteredList.clear();
        for (Job job : jobList) {
            if (job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(job);
            }
        }
        jobAdapter.notifyDataSetChanged();
    }
}
