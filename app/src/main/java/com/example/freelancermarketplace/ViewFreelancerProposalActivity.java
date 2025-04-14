package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.FreelancerProposalAdapter;
import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobCRUD;
import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.Classes.ProposalCRUD;
import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewFreelancerProposalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FreelancerProposalAdapter proposalAdapter;
    private List<Proposal> proposalList = new ArrayList<>();
    private Map<String, Job> jobMap = new HashMap<>();
    private String freelancerId;
    private Toolbar toolbar;
    List<User> list = new ArrayList<>();


    private ProposalCRUD proposalCRUD = new ProposalCRUD();
    private JobCRUD jobCRUD = new JobCRUD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_freelancer_proposal);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Proposals");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewFreelancerProposals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        freelancerId = getIntent().getStringExtra("userID");

        // FIRST: Load all jobs
        UserCRUD crudUser = new UserCRUD();


        crudUser.getAllUsers(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    list.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadAllJobsFirst();
    }

    private User getUserById(String id){

        User user =new User();
        for(User u:list){
            if(u.getUserId().equals(id)){
                user = u;
            }
        }
        return user;
    }
    private void loadAllJobsFirst() {
        jobCRUD.getAllJobs(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobMap.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Job job = data.getValue(Job.class);
                    if (job != null) {
                        jobMap.put(job.getJobId(), job);
                    }
                }
                // AFTER job list is ready, now load proposals
                loadProposals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFreelancerProposalActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProposals() {
        proposalCRUD.getAllProposals(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proposalList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Proposal proposal = data.getValue(Proposal.class);
                    if (proposal != null && proposal.getFreelancerId().equals(freelancerId)) {
                        // Only include if job exists
                        if (jobMap.containsKey(proposal.getJobId())) {
                            proposalList.add(proposal);
                        }
                    }
                }

                // Sort proposals by status
                Collections.sort(proposalList, (p1, p2) -> {
                    List<String> order = Arrays.asList("negotiating", "pending", "declined");
                    return Integer.compare(order.indexOf(p1.getStatus()), order.indexOf(p2.getStatus()));
                });

                // Set adapter
                proposalAdapter = new FreelancerProposalAdapter(ViewFreelancerProposalActivity.this, proposalList, jobMap,freelancerId,getUserById(freelancerId),list);
                recyclerView.setAdapter(proposalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFreelancerProposalActivity.this, "Failed to load proposals", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
