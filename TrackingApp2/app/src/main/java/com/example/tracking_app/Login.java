package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tracking-app-4f610-default-rtdb.firebaseio.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText employee_id = findViewById(R.id.employee_id);
        final EditText password = findViewById(R.id.password);
        final Button email_login_button = findViewById(R.id.email_login_button);

        email_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String employee_idTxt = employee_id.getText().toString();
                final String passwordTxt = password.getText().toString();
                if (employee_idTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your employee ID and password", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("employee").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(employee_idTxt)) {
                                final String getPassword = snapshot.child(employee_idTxt).child("password").getValue(String.class);
                                if (getPassword != null && getPassword.equals(passwordTxt)) {
                                    Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    // Pass the logged-in employee ID to MainActivity
                                    Intent intent = new Intent(Login.this, Choosing.class);
                                    intent.putExtra("EMPLOYEE_ID", employee_idTxt);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Wrong password entered", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Wrong employee ID", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Error retrieving data from database", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
