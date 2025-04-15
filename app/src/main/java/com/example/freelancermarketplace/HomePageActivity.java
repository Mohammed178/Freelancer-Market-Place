package com.example.freelancermarketplace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private RecyclerView recyclerView;
    private List<Job> jobList = new ArrayList<>();
    private String currentUserId;
    private String currentUserRole;
    private JobCRUD jobCRUD = new JobCRUD();
    private UserCRUD userCRUD = new UserCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);

        // Get user data from intent
        currentUserId = getIntent().getStringExtra("userId");
        currentUserRole = getIntent().getStringExtra("role");

        // Request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }

        // Load jobs based on user role
        loadJobsBasedOnRole();
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setTitle("Enable Notifications")
                    .setMessage("Allow us to send you important updates and job notifications?")
                    .setPositiveButton("Allow", (dialog, which) ->
                            ActivityCompat.requestPermissions(
                                    HomePageActivity.this,
                                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                    NOTIFICATION_PERMISSION_CODE))
                    .setNegativeButton("Later", null)
                    .show();
        }
    }

    private void loadJobsBasedOnRole() {
        jobCRUD.getAllJobs(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    if (shouldShowJob(job)) {
                        jobList.add(job);
                    }
                }
                recyclerView.setAdapter(new JobAdapter(jobList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showError("Failed to load jobs: " + error.getMessage());
            }
        });
    }

    private boolean shouldShowJob(Job job) {
        return ("freelancer".equals(currentUserRole) && currentUserId.equals(job.getFreelancerId())) ||
                ("client".equals(currentUserRole) && currentUserId.equals(job.getClientId()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        configureMenuBasedOnRole(menu);
        return true;
    }

    private void configureMenuBasedOnRole(Menu menu) {
        MenuItem addJobItem = menu.findItem(R.id.menu_add_job);
        MenuItem manageJobsItem = menu.findItem(R.id.menu_manage_jobs);
        MenuItem browseJobsItem = menu.findItem(R.id.menu_browse);
        MenuItem viewProposalItem = menu.findItem(R.id.menu_view_proposals);
        MenuItem viewFreelancerProposalItem = menu.findItem(R.id.menu_view_freelancer_proposals);

        if ("client".equals(currentUserRole)) {
            addJobItem.setVisible(true);
            manageJobsItem.setVisible(true);
            viewProposalItem.setVisible(true);
        } else if ("freelancer".equals(currentUserRole)) {
            viewFreelancerProposalItem.setVisible(true);
            browseJobsItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.menu_add_job) {
            intent = new Intent(this, AddJobActivity.class);
        } else if (id == R.id.menu_manage_jobs) {
            intent = new Intent(this, ManageJobsActivity.class);
        } else if (id == R.id.menu_logout) {
            handleLogout();
            return true;
        } else if (id == R.id.menu_browse) {
            intent = new Intent(this, BrowseJobsActivity.class);
        } else if (id == R.id.menu_view_proposals) {
            intent = new Intent(this, ViewProposalsActivity.class);
        } else if (id == R.id.menu_view_freelancer_proposals) {
            intent = new Intent(this, ViewFreelancerProposalActivity.class);
        }

        if (intent != null) {
            intent.putExtra("userID", currentUserId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}