package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobCRUD;
import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.Classes.ProposalAdapter;
import com.example.freelancermarketplace.Classes.ProposalCRUD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProposalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProposalAdapter adapter;
    private String currentUserID;
    private List<Proposal> proposalList = new ArrayList<>();

    // Mock maps for demonstration
    private final Map<String, Job> jobMap = new HashMap<>();
    private final Map<String, String> freelancerNames = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_proposals);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Adds a back button
        getSupportActionBar().setTitle("View your Proposals");
        currentUserID = getIntent().getStringExtra("userID");
        recyclerView = findViewById(R.id.recyclerViewProposals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProposalAdapter(this, proposalList, jobMap, freelancerNames, this::showProposalDialog);
        recyclerView.setAdapter(adapter);

        loadMockData();
    }
    private void loadMockData() {
        ProposalCRUD proposalCrud = new ProposalCRUD();
        JobCRUD jobCrud = new JobCRUD();

        List<Proposal> allProposals = new ArrayList<>();
        List<Job> jobsList = new ArrayList<>();

        // Get clientId passed from the previous activity
        String currentClientId = getIntent().getStringExtra("clientId");

        proposalCrud.getAllProposals(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProposals.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Proposal proposal = snap.getValue(Proposal.class);
                    allProposals.add(proposal);
                }

                // Load jobs only after proposals are loaded
                jobCrud.getAllJobs(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        jobsList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Job job = snap.getValue(Job.class);
                            jobsList.add(job);
                        }

                        // Now filter proposals
                        List<Proposal> filteredProposals = new ArrayList<>();
                        for (Proposal proposal : allProposals) {
                            if ("pending".equalsIgnoreCase(proposal.getStatus())) {
                                for (Job job : jobsList) {
                                    if (job.getJobId().equals(proposal.getJobId()) &&
                                            job.getClientId().equals(currentClientId)) {
                                        filteredProposals.add(proposal);
                                        break;
                                    }
                                }
                            }
                        }

                        adapter.updateList(filteredProposals);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewProposalsActivity.this, "Failed to load Jobs", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProposalsActivity.this, "Failed to load Proposals", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProposalDialog(Proposal proposal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Proposal Options")
                .setMessage("What would you like to do?")
                .setPositiveButton("Chat", (dialog, which) -> {
                    Intent chatIntent = new Intent(this, ChatPageActivity.class);
                    chatIntent.putExtra("freelancerId", proposal.getFreelancerId());
                    startActivity(chatIntent);
                })
                .setNegativeButton("Decline", (dialog, which) -> {
                    Toast.makeText(this, "Proposal Declined", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}