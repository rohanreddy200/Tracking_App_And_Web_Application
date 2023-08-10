package com.example.tracking_app;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private DatabaseReference databaseReference;
    private DatabaseReference currentLocationRef;

    private Button startLocationButton;
    private Button stopLocationButton;

    private boolean locationUpdatesStarted = false;
    private String loggedInEmployeeId; // Variable to hold the logged-in employee ID

    private long startTime;
    private SimpleDateFormat timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new MyLocationCallback();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("locations");

        startLocationButton = findViewById(R.id.startLocationButton);
        stopLocationButton = findViewById(R.id.stopLocationButton);

        // Get the logged-in employee ID from the intent extras
        loggedInEmployeeId = getIntent().getStringExtra("EMPLOYEE_ID");

        startLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        stopLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && locationUpdatesStarted) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000L);
        locationRequest.setFastestInterval(5000L);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (!locationUpdatesStarted) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

                // Generate a new file index for the location data
                startTime = System.currentTimeMillis();

                // Format the current date as yyyy-MM-dd
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Create a new child reference for the current employee's location file
                String startTimeFormatted = timeFormat.format(new Date(startTime));
                currentLocationRef = databaseReference.child("employee").child(loggedInEmployeeId)
                        .child(currentDate).child(startTimeFormatted);

                locationUpdatesStarted = true;
            } else {
                Toast.makeText(MainActivity.this, "Location updates already started", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        if (locationUpdatesStarted) {
            currentLocationRef = null;
            locationUpdatesStarted = false;
        }
    }

    private void saveLocationToDatabase(double latitude, double longitude) {
        if (locationUpdatesStarted && currentLocationRef != null) {
            DatabaseReference locationRef = currentLocationRef.push();
            String timestamp = timeFormat.format(new Date(System.currentTimeMillis() - startTime));
            locationRef.child("latitude").setValue(latitude);
            locationRef.child("longitude").setValue(longitude);
            locationRef.child("timestamp").setValue(timestamp);
        } else {
            Toast.makeText(MainActivity.this, "Location updates not started", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Toast.makeText(MainActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude,
                        Toast.LENGTH_SHORT).show();
                saveLocationToDatabase(latitude, longitude);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationUpdatesStarted) {
                    startLocationUpdates();
                }
            } else {
                Toast.makeText(MainActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
