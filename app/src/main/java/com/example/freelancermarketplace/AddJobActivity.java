package com.example.freelancermarketplace;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Job");
        }

        titleInput = findViewById(R.id.jobTitle);
        descriptionInput = findViewById(R.id.jobDescription);
        budgetInput = findViewById(R.id.jobBudget);
        postButton = findViewById(R.id.btnSubmitJob);

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

    // Override the onOptionsItemSelected to handle the back button action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
