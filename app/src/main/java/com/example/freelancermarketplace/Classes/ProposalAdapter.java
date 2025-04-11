package com.example.freelancermarketplace.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.R;

import java.util.List;
import java.util.Map;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {

    private final Context context;
    private final List<Proposal> proposals;
    private final Map<String, Job> jobMap;
    private final Map<String, String> freelancerNames;
    private final OnProposalClickListener listener;

    public interface OnProposalClickListener {
        void onProposalClick(Proposal proposal);
    }

    public ProposalAdapter(Context context, List<Proposal> proposals,
                           Map<String, Job> jobMap,
                           Map<String, String> freelancerNames,
                           OnProposalClickListener listener) {
        this.context = context;
        this.proposals = proposals;
        this.jobMap = jobMap;
        this.freelancerNames = freelancerNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proposal, parent, false);
        return new ProposalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalViewHolder holder, int position) {
        Proposal proposal = proposals.get(position);
        Job job = jobMap.get(proposal.getJobId());
        String freelancerName = freelancerNames.get(proposal.getFreelancerId());

        holder.tvJobTitle.setText(job != null ? job.getTitle() : "Unknown Job");
        holder.tvFreelancerName.setText("Freelancer: " + (freelancerName != null ? freelancerName : "Unknown"));
        holder.tvBidAmount.setText("Bid: $" + proposal.getBidAmount());

        holder.itemView.setOnClickListener(v -> listener.onProposalClick(proposal));
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    static class ProposalViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvFreelancerName, tvBidAmount;

        ProposalViewHolder(View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvFreelancerName = itemView.findViewById(R.id.tvFreelancerName);
            tvBidAmount = itemView.findViewById(R.id.tvBidAmount);
        }
    }
}

