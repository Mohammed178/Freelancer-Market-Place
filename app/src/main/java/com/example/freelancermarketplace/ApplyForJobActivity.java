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

import com.example.freelancermarketplace.Classes.Proposal;
import com.example.freelancermarketplace.Classes.ProposalCRUD;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ApplyForJobActivity extends AppCompatActivity{

    private static final int FILE_PICK_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private String currentUserID;
    private String currentJobID;
    private double budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_for_job);

        currentJobID = getIntent().getStringExtra("jobID");
        currentUserID = getIntent().getStringExtra("userID");
        budget = getIntent().getDoubleExtra("budget",0);

        Button attachButton = findViewById(R.id.attach_file_button);
        Button goBackButton = findViewById(R.id.goBack);
        Button submitButton = findViewById(R.id.submitButton);

//        Get the description text form the description box
        TextInputEditText descriptionInput = findViewById(R.id.description_box);
        String userInput = descriptionInput.getText().toString().trim();

//        Go back to Home Page
        goBackButton.setOnClickListener(v-> {
            Intent intent = new Intent(ApplyForJobActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
//        Choose file to upload when attach file is clicked
        attachButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                openFilePicker();
            } else {
                requestPermissions();
            }
        });



        submitButton.setOnClickListener(v -> {



            String description = descriptionInput.getText().toString().trim();


            if (description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double bidAmount;
            try {
                bidAmount = budget;
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid bid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            String proposalId = UUID.randomUUID().toString();
            Proposal proposal = new Proposal(
                    proposalId,
                    currentJobID,
                    currentUserID,
                    bidAmount,
                    description,
                    System.currentTimeMillis(),
                    "pending"
            );


            ProposalCRUD crud = new ProposalCRUD();
            crud.saveProposal(proposal);

            Toast.makeText(this, "Proposal submitted!", Toast.LENGTH_SHORT).show();
            finish();
        });
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

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return true;
        } else {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void handleSelectedFile(Uri uri) {
        try {
            String filePath = getFilePathFromUri(uri);
            Toast.makeText(this, "Selected file: " + filePath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("proposals/" + currentUserID + "/" + System.currentTimeMillis());

        fileRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        String fileUrl = downloadUri.toString();
                        // Save this fileUrl in your Proposal or separately
                        Toast.makeText(this, "File uploaded!", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });*/

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
