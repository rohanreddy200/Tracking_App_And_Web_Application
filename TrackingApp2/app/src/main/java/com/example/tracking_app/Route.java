package com.example.tracking_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Route extends AppCompatActivity {

    private Button routeDetailsButton;
    private TextView fromTextView;
    private TextView toTextView;

    // Firebase database reference
    private DatabaseReference database;

    // Logged-in employee ID (example value)
    private String loggedInEmployeeId;

    // Current date
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("routes");

        // Get reference to the "Route Details" button and TextViews
        routeDetailsButton = findViewById(R.id.routeDetailsButton);
        fromTextView = findViewById(R.id.fromTextView);
        toTextView = findViewById(R.id.toTextView);

        // Set a click listener for the "Route Details" button
        routeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveRouteDetails();
            }
        });

        // Get the logged-in employee ID from the login activity (replace with your logic)
        loggedInEmployeeId = getIntent().getStringExtra("EMPLOYEE_ID");

        // Get the current date
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    // Method to retrieve route details from the database
    private void retrieveRouteDetails() {
        // Construct the path to the route details for the current date and employee ID
        String routePath = currentDate + "/" + loggedInEmployeeId;

        // Retrieve the route details from the database
        database.child(routePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Route details found
                    String from = dataSnapshot.child("from").getValue(String.class);
                    String to = dataSnapshot.child("to").getValue(String.class);

                    // Display the route details in the TextViews
                    fromTextView.setText("From: " + from);
                    toTextView.setText("To: " + to);
                } else {
                    // Route details not found
                    Toast.makeText(Route.this, "No route details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving route details
                Toast.makeText(Route.this, "Error retrieving route details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
