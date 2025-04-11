package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.Classes.ProposalAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProposalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProposalAdapter adapter;
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
        recyclerView = findViewById(R.id.recyclerViewProposals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProposalAdapter(this, proposalList, jobMap, freelancerNames, this::showProposalDialog);
        recyclerView.setAdapter(adapter);

        loadMockData();
    }
    private void loadMockData() {
        // Add job info and freelancer names
        jobMap.put("job1", new Job("job1", "Logo Design", "Create a logo", 100.0, "open", "client123", null, System.currentTimeMillis()));
        freelancerNames.put("freelancer1", "Alice");

        // Add sample proposal
        proposalList.add(new Proposal("proposal1", "job1", "freelancer1", 90.0, "I can do it!", System.currentTimeMillis(), "pending"));
        adapter.notifyDataSetChanged();
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