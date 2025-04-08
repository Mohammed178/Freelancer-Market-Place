package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        TextInputEditText txtEmail = findViewById(R.id.txtEmail);
        TextInputEditText txtPassword = findViewById(R.id.txtPassword);
        TextInputEditText txtPasswordConfirm = findViewById(R.id.txtPasswordConfirm);

        TextView signUpText = findViewById(R.id.login_link);
        signUpText.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        Button next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String confirmPassword = txtPasswordConfirm.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass the inputs to FinishSignUpActivity
            Intent i = new Intent(SignUpActivity.this, FinishSignUpActivity.class);
            i.putExtra("email", email);
            i.putExtra("password", password);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
