package com.example.tracking_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;

public class Choosing extends AppCompatActivity implements View.OnClickListener {
    private CardView tasksCardView;
    private CardView locationTrackingCardView;
    private CardView routeDetailsCardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        tasksCardView = findViewById(R.id.tasksCardView);
        locationTrackingCardView = findViewById(R.id.locationTrackingCardView);
        routeDetailsCardView = findViewById(R.id.RouteDetailsCardView); // Corrected ID

        tasksCardView.setOnClickListener(this);
        locationTrackingCardView.setOnClickListener(this);
        routeDetailsCardView.setOnClickListener(this); // Add click listener for the Route Details CardView
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tasksCardView:
                intent = new Intent(Choosing.this, Tasks.class);
                startActivity(intent);
                break;
            case R.id.locationTrackingCardView:
                intent = new Intent(Choosing.this, MainActivity.class);
                intent.putExtra("EMPLOYEE_ID", getIntent().getStringExtra("EMPLOYEE_ID"));
                startActivity(intent);
                break;
            case R.id.RouteDetailsCardView: // Corrected ID
                intent = new Intent(Choosing.this, Route.class);
                intent.putExtra("EMPLOYEE_ID", getIntent().getStringExtra("EMPLOYEE_ID"));
                startActivity(intent);
                break;
        }
    }
}
