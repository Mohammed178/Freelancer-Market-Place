package com.example.freelancermarketplace.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.R;

import java.util.List;
import java.util.Map;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {

    private final Context context;
    private List<Proposal> proposals;

private User loggedInUser;
    private final Map<String, Job> jobMap;
    private final Map<String, User> userMap; // Replace freelancerNames with actual User objects
    private final OnProposalClickListener listener;

    public interface OnProposalClickListener {
        void onProposalClick(Proposal proposal,User freelancer, User loggedInUser);
    }

    public ProposalAdapter(Context context,
                           List<Proposal> proposals,
                           Map<String, Job> jobMap,
                           Map<String, User> userMap,
                           User loggedInUser,
                           OnProposalClickListener listener) {
        this.context = context;
        this.proposals = proposals;
        this.jobMap = jobMap;
        this.userMap = userMap;
        this.loggedInUser = loggedInUser;
        this.listener = listener;
    }

    public void updateList(List<Proposal> newList) {
        this.proposals = newList;
        notifyDataSetChanged();
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
        User freelancer = userMap.get(proposal.getFreelancerId());

        holder.tvJobTitle.setText(job != null ? job.getTitle() : "Job not found");
        holder.tvFreelancerName.setText("Freelancer: " + (freelancer != null ? freelancer.getName() : "Name not available"));
        holder.tvBidAmount.setText("RM " + proposal.getBidAmount());

        if (freelancer != null && freelancer.getProfilePicUrl() != null && !freelancer.getProfilePicUrl().isEmpty()) {
            Glide.with(context)
                    .load(freelancer.getProfilePicUrl())
                    .placeholder(R.drawable.ic_user_placeholder)
                    .circleCrop()
                    .into(holder.imgProfile);
        } else {
            holder.imgProfile.setImageResource(R.drawable.ic_user_placeholder);
        }

        holder.itemView.setOnClickListener(v -> listener.onProposalClick(proposal,freelancer,loggedInUser));
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    static class ProposalViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvFreelancerName, tvBidAmount;
        ImageView imgProfile;

        ProposalViewHolder(View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvFreelancerName = itemView.findViewById(R.id.tvFreelancerName);
            tvBidAmount = itemView.findViewById(R.id.tvBidAmount);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }
}
