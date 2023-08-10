package com.example.tracking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Tasks extends AppCompatActivity {
   RecyclerView recyclerView;
   Task_adapter task_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Tasks"), Task.class)
                        .build();

        task_adapter= new Task_adapter(options);
        recyclerView.setAdapter(task_adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        task_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        task_adapter.stopListening();
    }
}