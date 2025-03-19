package com.example.freelancermarketplace;


import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView signUpText = findViewById(R.id.signup_link);
//        Link the Signup text to the Signup page
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v-> {
            Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

}
