package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.freelancermarketplace.Classes.Bridge;
import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.android.material.textfield.TextInputEditText;
public class UpdateUserDataActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etEmail, etPhone, etNewPassword, etConfirmPassword;
    private Button btnSave;
    String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_data);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);

        // Setup go back button to return to the previous page
        ImageButton goBackButton = findViewById(R.id.backButton);
        goBackButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        currentId = getIntent().getStringExtra("userID");

        User user = Bridge.getUserById(currentId);
        loadUserData(user);
        btnSave.setOnClickListener(v -> validateAndSave(user));
    }

    private void loadUserData(User user) {
        etFirstName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getContact());
    }

    private void validateAndSave(User user) {
        // Get input values
        String firstName = etFirstName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return;
        }

        if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
            etPhone.setError("Valid phone number is required");
            return;
        }

        // Check if the new password field is empty
        if (newPassword.isEmpty()) {
            etNewPassword.setError("Please type a new password");
            return;
        }

        // Validate password length and confirmation if password is provided
        if (newPassword.length() < 8) {
            etNewPassword.setError("Password must be at least 8 characters");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // If all validations pass, update the user profile
        updateUserProfile(firstName, email, phone, newPassword, user);
    }

    private void updateUserProfile(String firstName, String email, String phone, String newPassword, User user) {
        user.setName(firstName);
        user.setEmail(email);
        user.setContact(phone);
        user.setPassword(newPassword);

        new UserCRUD().updateUser(user.getUserId(), user);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
