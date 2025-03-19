package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

//        Link the Login text to the Login page
        TextView signUpText = findViewById(R.id.login_link);
        signUpText.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this,
                    LoginActivity.class);
            startActivity(i);

//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        Button next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
                Intent i = new Intent(SignUpActivity.this,
                        FinishSignUpActivity.class);
                startActivity(i);

//            Add animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }
}