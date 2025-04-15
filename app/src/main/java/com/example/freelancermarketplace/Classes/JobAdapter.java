package com.example.freelancermarketplace.Classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.freelancermarketplace.ChatPageActivity;
import com.example.freelancermarketplace.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobs;
    private Context context;
    User cliente;
    User userLogged;
    private String currentUserId;
    private Proposal proposal;

    public JobAdapter(List<Job> jobs, Context context, String currentUserId) {
        this.jobs = jobs;
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.title.setText(job.getTitle());
        holder.desc.setText(job.getDescription());
        holder.budget.setText("RM " + job.getBudget());

        // Fetch client name using UserCRUD
        new UserCRUD().getUser(job.getClientId(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User client = snapshot.getValue(User.class);
                if (client != null) {
                    cliente = client;
                    holder.clientName.setText("Client: " + client.getName() +"\nStatus:"+job.getStatus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.clientName.setText("Client: Unknown");
            }
        });

        userLogged = Bridge.getUserById(currentUserId);

        if ("in progress".equalsIgnoreCase(job.getStatus())) {
            holder.markAsDoneBtn.setEnabled(true);
            holder.markAsDoneBtn.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_variant));
        } else {
            holder.markAsDoneBtn.setEnabled(false);
            holder.markAsDoneBtn.setBackgroundColor(Color.GRAY);
        }

        if (userLogged != null) {

            if ("freelancer".equalsIgnoreCase(userLogged.getRole())) {
                holder.markAsDoneBtn.setVisibility(View.VISIBLE);
                holder.approveBtn.setVisibility(View.GONE);
                holder.rejectBtn.setVisibility(View.GONE);

                if ("in progress".equalsIgnoreCase(job.getStatus())) {
                    holder.markAsDoneBtn.setEnabled(true);
                    holder.markAsDoneBtn.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_variant));
                } else if("pending".equalsIgnoreCase(job.getStatus())){
                    holder.markAsDoneBtn.setEnabled(false);
                    holder.markAsDoneBtn.setBackgroundColor(Color.GRAY);
                }else if("concluded".equalsIgnoreCase(job.getStatus())){
                    holder.markAsDoneBtn.setVisibility(View.GONE);
                    holder.messageBtn.setVisibility(View.GONE);

                    Review review = Bridge.getReviewbyJobID(job.getJobId());
                    if(review!=null){
                        holder.rated.setVisibility(View.VISIBLE);
                        holder.rated.setRating(review.getRating());
                        holder.rated.setEnabled(false);
                        holder.reviewCommentTXT.setText(review.getComments());
                        holder.reviewCommentTXT.setEnabled(false);
                        holder.reviewCommentTXT.setVisibility(View.VISIBLE);
                    }

                }

            } else if ("client".equalsIgnoreCase(userLogged.getRole())) {
                holder.markAsDoneBtn.setVisibility(View.GONE);

                if ("pending".equalsIgnoreCase(job.getStatus())) {

                    holder.approveBtn.setVisibility(View.VISIBLE);
                    holder.rejectBtn.setVisibility(View.VISIBLE);
                } else {
                    holder.approveBtn.setVisibility(View.GONE);
                    holder.rejectBtn.setVisibility(View.GONE);
                }
            }
        }

        new ProposalCRUD().getAllProposals(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Proposal pr = snap.getValue(Proposal.class);
                    if(userLogged.getRole().equals("freelancer")){
                        if(pr.getJobId().equals(job.getJobId()) && pr.getFreelancerId().equals(userLogged.getUserId())){
                            proposal = pr;
                            break;
                        }
                    }else {
                        if(pr.getJobId().equals(job.getJobId()) ){
                            proposal = pr;
                            break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.messageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatPageActivity.class);

            intent.putExtra("userID", currentUserId);

            intent.putExtra("freelancer", userLogged);
            intent.putExtra("client", cliente);
            intent.putExtra("proposal", proposal);


            context.startActivity(intent);
        });

        holder.markAsDoneBtn.setOnClickListener(v -> {
            job.setStatus("pending");
            new JobCRUD().updateJob(job.getJobId(), job);
            Toast.makeText(context, "Marked as Done (Pending Approval)", Toast.LENGTH_SHORT).show();
            notifyItemChanged(position);


        });

        holder.approveBtn.setOnClickListener(v -> {
            job.setStatus("concluded");
            new JobCRUD().updateJob(job.getJobId(), job);
            Toast.makeText(context, "Job Approved", Toast.LENGTH_SHORT).show();
            notifyItemChanged(position);
            showReviewDialog(job, proposal.getFreelancerId());
        });




        holder.rejectBtn.setOnClickListener(v -> {
            job.setStatus("in progress");
            new JobCRUD().updateJob(job.getJobId(), job);
            Toast.makeText(context, "Job Rejected. Moved to In Progress", Toast.LENGTH_SHORT).show();
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    private void showReviewDialog(Job job, String freelancerId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.review_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        TextView freelancerNameView = dialogView.findViewById(R.id.freelancerName);
        TextView jobTitleView = dialogView.findViewById(R.id.jobTitle);
        TextView jobBudgetView = dialogView.findViewById(R.id.jobBudget);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText commentsView = dialogView.findViewById(R.id.reviewComments);
        Button submitBtn = dialogView.findViewById(R.id.submitReviewBtn);
        Button skipBtn = dialogView.findViewById(R.id.skipReviewBtn);

        freelancerNameView.setText("Freelancer: Loading...");
        jobTitleView.setText("Job: " + job.getTitle());
        jobBudgetView.setText("Budget: RM " + job.getBudget());

        // Load freelancer name
        new UserCRUD().getUser(freelancerId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User freelancer = snapshot.getValue(User.class);
                if (freelancer != null) {
                    freelancerNameView.setText("Freelancer: " + freelancer.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        submitBtn.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String comments = commentsView.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(context, "Please give at least 1 star.", Toast.LENGTH_SHORT).show();
                return;
            }

            Review review = new Review();
            review.setReviewId(String.valueOf(System.currentTimeMillis()));
            review.setJobId(job.getJobId());
            review.setClientId(currentUserId);
            review.setFreelancerId(freelancerId);
            review.setRating(rating);
            review.setComments(comments);
            review.setTimestamp(System.currentTimeMillis());

            new ReviewCRUD().saveReview(review);
            Toast.makeText(context, "Review submitted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        skipBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, budget, clientName, approveBtn,rejectBtn,reviewCommentTXT;
        Button messageBtn,markAsDoneBtn;

        RatingBar rated ;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.jobTitle);
            desc = itemView.findViewById(R.id.jobDesc);
            budget = itemView.findViewById(R.id.jobBudget);
            clientName = itemView.findViewById(R.id.clientName);
            messageBtn = itemView.findViewById(R.id.messageBtn);
            markAsDoneBtn = itemView.findViewById(R.id.markAsDoneBtn);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            rated = itemView.findViewById(R.id.ratingBar);
            reviewCommentTXT = itemView.findViewById(R.id.ratingComment);
        }
    }
}
