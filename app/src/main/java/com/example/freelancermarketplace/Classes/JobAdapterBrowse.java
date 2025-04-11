package com.example.freelancermarketplace;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Job;

import java.util.List;

public class JobAdapterBrowse extends RecyclerView.Adapter<JobAdapterBrowse.JobViewHolder> {

    private List<Job> jobList;
    private String userId;

    public JobAdapterBrowse(List<Job> jobs, String userId) {
        this.jobList = jobs;
        this.userId = userId;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.title.setText(job.getTitle());
        holder.description.setText(job.getDescription());
        holder.budget.setText("Budget: RM " + job.getBudget());

        holder.applyButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            Intent i = new Intent(context, ApplyForJobActivity.class);
            i.putExtra("jobID", job.getJobId());
            i.putExtra("userID", userId);
            i.putExtra("budget", job.getBudget());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, budget;
        Button applyButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtJobTitle);
            description = itemView.findViewById(R.id.txtJobDescription);
            budget = itemView.findViewById(R.id.txtJobBudget);
            applyButton = itemView.findViewById(R.id.btnApply);
        }
    }
}
