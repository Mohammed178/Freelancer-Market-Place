package com.example.freelancermarketplace;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelancermarketplace.Classes.Job;
import com.example.freelancermarketplace.Classes.JobCRUD;

import java.util.UUID;

public class AddJobActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput, budgetInput;
    private Button postButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        titleInput = findViewById(R.id.job_title_input);
        descriptionInput = findViewById(R.id.job_description_input);
        budgetInput = findViewById(R.id.job_budget_input);
        postButton = findViewById(R.id.post_job_button);
        String clientId = getIntent().getStringExtra("userID");

        postButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String budgetStr = budgetInput.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty() || budgetStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double budget = Double.parseDouble(budgetStr);
            String jobId = UUID.randomUUID().toString();

            Job job = new Job(jobId, title, description, budget, "open", clientId, null, System.currentTimeMillis());
            new JobCRUD().saveJob(job);
            Toast.makeText(this, "Job Posted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
