package com.example.freelancermarketplace.Classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.ChatPageActivity;
import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.R;

import java.util.List;
import java.util.Map;

public class FreelancerProposalAdapter extends RecyclerView.Adapter<FreelancerProposalAdapter.ViewHolder> {

    private Context context;
    private List<Proposal> proposals;
    private Map<String, Job> jobMap;
private List<User> userList;
    private User freelancer;

    private String currentUserID;

    public FreelancerProposalAdapter(Context context, List<Proposal> proposals, Map<String, Job> jobMap, String currentUserId,User freelancer,List<User> userList) {
        this.context = context;
        this.proposals = proposals;
        this.currentUserID = currentUserId;
        this.jobMap = jobMap;
        this.freelancer = freelancer;
        this.userList = userList;
    }
    private User getUserById(String id){

        User user =new User();
        for(User u:userList){
            if(u.getUserId().equals(id)){
                user = u;
            }
        }
        return user;
    }

    @NonNull
    @Override
    public FreelancerProposalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_freelancer_proposal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreelancerProposalAdapter.ViewHolder holder, int position) {
        Proposal proposal = proposals.get(position);
        Job job = jobMap.get(proposal.getJobId());

        if (job != null) {
            holder.tvTitle.setText(job.getTitle());
            holder.tvBudget.setText("Budget: $" + job.getBudget());
            holder.tvStatus.setText("Status: " + proposal.getStatus());
            holder.tvMessage.setText("Message: " + proposal.getProposalMessage());

            holder.itemView.setOnClickListener(v -> {
                if ("negotiating".equals(proposal.getStatus())) {
                    Intent intent = new Intent(context, ChatPageActivity.class);

                    intent.putExtra("freelancer", freelancer);
                    intent.putExtra("client", getUserById(job.getClientId()));
                    intent.putExtra("proposal", proposal);
                    intent.putExtra("userID", currentUserID);

                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBudget, tvStatus, tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvStatus = itemView.findViewById(R.id.tvProposalStatus);
            tvMessage = itemView.findViewById(R.id.tvProposalMessage);
        }
    }
}
