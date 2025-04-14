package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
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
    User loggedInUser;

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

        UserCRUD userCRUD = new UserCRUD();
        Map<String, User> userMap = new HashMap<>();

        userCRUD.getAllUsers(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);

                    if (user != null) {
                        userMap.put(user.getUserId(), user);
                    }
                    if(user.getUserId().equals(currentUserID)){
                        loggedInUser = user;
                    }
                }

                // After users are loaded, pass to adapter
                adapter = new ProposalAdapter(ViewProposalsActivity.this, proposalList, jobMap, userMap,loggedInUser, ViewProposalsActivity.this::showProposalDialog);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProposalsActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });

        loadMockData();
    }
    private void loadMockData() {
        ProposalCRUD proposalCrud = new ProposalCRUD();
        JobCRUD jobCrud = new JobCRUD();

        String currentClientId = getIntent().getStringExtra("userID");

        // First: Load all jobs
        jobCrud.getAllJobs(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                jobMap.clear();
                for (DataSnapshot snap : jobSnapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    if (job != null) {
                        jobMap.put(job.getJobId(), job);
                    }
                }

                // After jobs are loaded, load all proposals
                proposalCrud.getAllProposals(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot proposalSnapshot) {
                        proposalList.clear();
                        for (DataSnapshot snap : proposalSnapshot.getChildren()) {
                            Proposal proposal = snap.getValue(Proposal.class);
                            if (proposal != null && "pending".equalsIgnoreCase(proposal.getStatus())) {
                                Job job = jobMap.get(proposal.getJobId());
                                if (job != null && job.getClientId().equals(currentClientId)) {
                                    proposalList.add(proposal);
                                }
                            }
                        }

                        adapter.updateList(proposalList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewProposalsActivity.this, "Failed to load Proposals", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProposalsActivity.this, "Failed to load Jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProposalDialog(Proposal proposal,User freelancer,User loggedInUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_proposal_options, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button btnChat = dialogView.findViewById(R.id.btnChat);
        Button btnDecline = dialogView.findViewById(R.id.btnDecline);

        btnChat.setOnClickListener(v -> {
            Intent chatIntent = new Intent(this, ChatPageActivity.class);
            chatIntent.putExtra("freelancer", freelancer);
            chatIntent.putExtra("client", loggedInUser);
            chatIntent.putExtra("proposal", proposal);
            startActivity(chatIntent);
            dialog.dismiss();
        });

        btnDecline.setOnClickListener(v -> {
            updateProposalStatusToDeclined(proposal);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void updateProposalStatusToDeclined(Proposal proposal) {
      ProposalCRUD ccrud = new ProposalCRUD();
      ccrud.updateProposal(proposal.getProposalId(),proposal);
    }
}