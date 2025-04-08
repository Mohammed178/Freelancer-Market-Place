package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class FinishSignUpActivity extends AppCompatActivity {

    private String selectedRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_signup);

        // Incoming data from previous activity
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        // Form fields
        TextInputEditText txtFirstName = findViewById(R.id.txtFirstName);
        TextInputEditText txtLastName = findViewById(R.id.txtLastName);
        TextInputEditText txtContact = findViewById(R.id.txtPhone);
        TextInputEditText txtID = findViewById(R.id.txtID);
        Spinner idTypeSpinner = findViewById(R.id.id_type_spinner);

        Button freelancerButton = findViewById(R.id.freelancer_button);
        Button customerButton = findViewById(R.id.customer_button);
        Button completeButton = findViewById(R.id.finish_button);

        // Spinner setup
        String[] idTypes = {"Passport", "Driver's License", "National ID", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idTypeSpinner.setAdapter(adapter);

        // Button selection
        freelancerButton.setOnClickListener(v -> {
            selectedRole = "freelancer";
            highlightSelectedButton(freelancerButton, customerButton);
        });

        customerButton.setOnClickListener(v -> {
            selectedRole = "client";
            highlightSelectedButton(customerButton, freelancerButton);
        });

        // Save user on completion
        completeButton.setOnClickListener(v -> {
            String firstName = txtFirstName.getText().toString().trim();
            String lastName = txtLastName.getText().toString().trim();
            String contact = txtContact.getText().toString().trim();
            String idNumber = txtID.getText().toString().trim();
            String idType = idTypeSpinner.getSelectedItem().toString();

            if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || idNumber.isEmpty() || selectedRole == null) {
                Toast.makeText(this, "Please fill in all fields and select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create and save user
            User user = new User();
            user.setUserId(idNumber);
            user.setContact(contact);
            user.setProfilePicUrl("-");
            user.setName(firstName + " " + lastName);
            user.setEmail(email);
            user.setPassword(password); // Ensure password is part of User class
            user.setRole(selectedRole);

            // Optional: set contact, idType, idNumber if User class has fields for them

            UserCRUD userCRUD = new UserCRUD();
            userCRUD.saveUser(user);

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            // Redirect to login
            Intent intent = new Intent(FinishSignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    private void highlightSelectedButton(Button selected, Button other) {
        selected.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        selected.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        other.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary));
        other.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }
}
