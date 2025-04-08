package com.example.freelancermarketplace.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProposalCRUD {
    private DatabaseReference databaseReference;

    public ProposalCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("proposals");
    }

    public void saveProposal(Proposal proposal) {
        databaseReference.child(proposal.getProposalId()).setValue(proposal);
    }

    public void getProposal(String proposalId, final ValueEventListener listener) {
        databaseReference.child(proposalId).addListenerForSingleValueEvent(listener);
    }

    public void updateProposal(String proposalId, Proposal updatedProposal) {
        databaseReference.child(proposalId).setValue(updatedProposal);
    }

    public void deleteProposal(String proposalId) {
        databaseReference.child(proposalId).removeValue();
    }

    public void getAllProposals(final ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(listener);
    }
}
