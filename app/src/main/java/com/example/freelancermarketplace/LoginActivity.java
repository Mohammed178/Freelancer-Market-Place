package com.example.freelancermarketplace;


import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelancermarketplace.Classes.User;
import com.example.freelancermarketplace.Classes.UserCRUD;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView signUpText = findViewById(R.id.signup_link);
        TextInputEditText txtEmail = findViewById(R.id.txtEmail);
        TextInputEditText txtPassword = findViewById(R.id.txtPassword);
        List<User> users = new ArrayList<>();
//        Link the Signup text to the Signup page
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });


        UserCRUD c = new UserCRUD();
        c.getAllUsers(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User job = snapshot1.getValue(User.class);
                    users.add(job);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this,"Failed to load the data! Try again",Toast.LENGTH_LONG).show();
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            String emailInput = txtEmail.getText().toString().trim();
            String passwordInput = txtPassword.getText().toString().trim();

            // Check if fields are empty
            if (emailInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate against user list
            boolean isValid = false;
            User loggedInUser = null;

            for (User user : users) {
                if (user.getEmail().equals(emailInput) && user.getPassword().equals(passwordInput)) {
                    isValid = true;
                    loggedInUser = user;
                    break;
                }
            }

            if (isValid) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                i.putExtra("userId", loggedInUser.getUserId());
                i.putExtra("userName", loggedInUser.getName());
                startActivity(i);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
