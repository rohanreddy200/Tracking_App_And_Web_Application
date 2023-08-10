package com.example.tracking_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Task_adapter extends FirebaseRecyclerAdapter<Task,Task_adapter.mytaskhoder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Task_adapter(@NonNull FirebaseRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull mytaskhoder holder, int position, @NonNull Task model) {
      holder.task.setText(model.getTask());

    }

    @NonNull
    @Override
    public mytaskhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_content_tasks,parent,false);
        return new mytaskhoder(view);
    }

    class mytaskhoder extends RecyclerView.ViewHolder{
        TextView task;

        public mytaskhoder(@NonNull View itemView) {
            super(itemView);
            task=(TextView)itemView.findViewById(R.id.nametext);
        }
    }
}
