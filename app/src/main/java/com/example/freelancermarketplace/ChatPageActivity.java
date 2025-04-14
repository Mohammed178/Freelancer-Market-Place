package com.example.freelancermarketplace;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.Classes.ProposalCRUD;
import com.example.freelancermarketplace.Classes.User;
import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Java implementation
public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private EditText messageInput;
    private User freelancer;
    private User client;
    private Proposal proposal;
    private DatabaseReference messagesRef;

    private Button sendButton;
    private static final int FILE_PICK_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        freelancer =(User)getIntent().getSerializableExtra("freelancer");
        client =(User)getIntent().getSerializableExtra("client");
        proposal =(Proposal) getIntent().getSerializableExtra("proposal");
        if("pending".equalsIgnoreCase(proposal.getStatus())){
            proposal.setStatus("negotiating");
            ProposalCRUD c = new ProposalCRUD();
            c.updateProposal(proposal.getProposalId(),proposal);
        }
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");

//    Show messages on the chat page RecyclerView
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.send_message_button);

        // Setup RecyclerView
        adapter = new MessageAdapter(messages,getLoggedInUserId());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                String proposalId = proposal.getProposalId();
                String senderId, receiverId;

                // Determine sender and receiver based on logged-in user
                if (freelancer.getUserId().equals(getLoggedInUserId())) {
                    senderId = freelancer.getUserId();
                    receiverId = client.getUserId();
                } else {
                    senderId = client.getUserId();
                    receiverId = freelancer.getUserId();
                }

                String messageId = FirebaseDatabase.getInstance().getReference("messages")
                        .child(proposalId).push().getKey();

                long timestamp = System.currentTimeMillis();

                Message message = new Message(messageId, proposalId, senderId, receiverId, messageText, timestamp);

                // Save message to Firebase
                FirebaseDatabase.getInstance().getReference("messages")
                        .child(proposalId)
                        .child(messageId)
                        .setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            messageInput.getText().clear();
                            /*messages.add(message);
                            adapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.smoothScrollToPosition(messages.size() - 1);*/
                        })
                        .addOnFailureListener(e -> Toast.makeText(ChatPageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());
            }
        });

//        Initialize buttons
        Button attachButton = findViewById(R.id.attach_file_button);
        ImageButton goBackButton = findViewById(R.id.backButton);

        //        Go back to Home Page
        goBackButton.setOnClickListener(v-> {
            Intent intent = new Intent(ChatPageActivity.this, HomePageActivity.class);
            startActivity(intent);

//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        attachButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                openFilePicker();
            } else {
                requestPermissions();
            }
        });

        DatabaseReference messagesRef = FirebaseDatabase.getInstance()
                .getReference("messages").child(proposal.getProposalId());

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Message msg = snap.getValue(Message.class);
                    messages.add(msg);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatPageActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getLoggedInUserId() {
        // Replace with actual logic from session/local storage
        return getIntent().getStringExtra("userID");  // or use a SessionManager
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return true;
        } else {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                FILE_PICK_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                handleSelectedFile(data.getData());
            }
        }
    }

    private void handleSelectedFile(Uri uri) {
        try {
            String filePath = getFilePathFromUri(uri);
            Toast.makeText(this, "Selected file: " + filePath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilePathFromUri(Uri uri) {
        // Same logic as Kotlin version
        return "None";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            }
        }
    }
}
