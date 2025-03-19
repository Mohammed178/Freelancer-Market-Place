package com.example.freelancermarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class FinishSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_signup);

        // Set up ID Type Spinner
        Spinner idTypeSpinner = findViewById(R.id.id_type_spinner);
        String[] idTypes = {"Passport", "Driver's License", "National ID", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idTypeSpinner.setAdapter(adapter);

        // Handle Spinner Selection
        idTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIdType = idTypes[position];
                // Do something with the selected ID type
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });
    }

//    When the user clicks an option
    public void onOptionSelected(View view) {
        Button freelancerButton = findViewById(R.id.freelancer_button);
        Button customerButton = findViewById(R.id.customer_button);

        if (view.getId() == R.id.freelancer_button) {
            freelancerButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
            freelancerButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            customerButton.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary));
            customerButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        } else if (view.getId() == R.id.customer_button) {
            customerButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
            customerButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            freelancerButton.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary));
            freelancerButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }
}
