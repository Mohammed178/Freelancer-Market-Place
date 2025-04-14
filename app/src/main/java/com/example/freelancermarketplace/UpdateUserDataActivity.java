package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.android.material.textfield.TextInputEditText;
public class UpdateUserDataActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etEmail, etPhone, etNewPassword, etConfirmPassword;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_data);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);

        // Load existing user data (replace with your actual data loading logic)
        loadUserData();

        btnSave.setOnClickListener(v -> validateAndSave());
    }

    private void loadUserData() {
        // Example data - replace with actual user data retrieval
        etFirstName.setText("John");
        etLastName.setText("Doe");
        etEmail.setText("john.doe@example.com");
        etPhone.setText("+1234567890");
    }

    private void validateAndSave() {
        // Get input values
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            return;
        }

        if (lastName.isEmpty()) {
            etLastName.setError("Last name is required");
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return;
        }

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            etPhone.setError("Valid phone number is required");
            return;
        }

        if (!newPassword.isEmpty()) {
            if (newPassword.length() < 8) {
                etNewPassword.setError("Password must be at least 8 characters");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }
        }

        // If all validations pass
        updateUserProfile(
                firstName,
                lastName,
                email,
                phone,
                newPassword
        );
    }

    private void updateUserProfile(String firstName, String lastName,
                                   String email, String phone, String newPassword) {
        // Implement your update logic here
        // This could be an API call or database update

        // For demonstration purposes:
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        // Consider adding progress indicators and error handling
    }
}
