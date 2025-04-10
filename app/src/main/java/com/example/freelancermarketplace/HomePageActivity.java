package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobAdapter;
import com.example.freelancermarketplace.Classes.JobCRUD;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Job> jobList = new ArrayList<>();
    private String currentUserId;
    private String currentUserRole;
    private JobCRUD jobCRUD = new JobCRUD(); // You'll need to implement this
    private UserCRUD userCRUD = new UserCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);


        currentUserId = getIntent().getStringExtra("userId");
        currentUserRole = getIntent().getStringExtra("role");

        if ("freelancer".equals(currentUserRole)) {
            jobCRUD.getAllJobs(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Job job = snap.getValue(Job.class);
                        if (currentUserId.equals(job.getFreelancerId())) {
                            jobList.add(job);
                        }
                    }
                    recyclerView.setAdapter(new JobAdapter(jobList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomePageActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
                }
            });
        } else if ("client".equals(currentUserRole)) {
            jobCRUD.getAllJobs(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Job job = snap.getValue(Job.class);
                        if (currentUserId.equals(job.getClientId())) {
                            jobList.add(job);
                        }
                    }
                    recyclerView.setAdapter(new JobAdapter(jobList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomePageActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Check user role and adjust menu visibility
        MenuItem addJobItem = menu.findItem(R.id.menu_add_job);
        MenuItem manageJobsItem = menu.findItem(R.id.menu_manage_jobs);
        MenuItem browseJobsItem = menu.findItem(R.id.menu_browse);
        if ("client".equals(currentUserRole)) {
            // Show Client-specific options
            addJobItem.setVisible(true);
            manageJobsItem.setVisible(true);
        }
        if("freelancer".equals(currentUserRole)){
            browseJobsItem.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_job) {
            startActivity(new Intent(this, AddJobActivity.class).putExtra("userID",currentUserId));
            return true;

        } else if (id == R.id.menu_manage_jobs) {
            startActivity(new Intent(this, ManageJobsActivity.class).putExtra("userID",currentUserId));
            return true;

        } else if (id == R.id.menu_logout) {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_browse) {
            
        }

        return super.onOptionsItemSelected(item);
    }
}
