package com.example.selfcareapp.ui.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<TaskEntity> tasks;

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull TaskViewHolder holder, int position){
        TaskEntity task = tasks.get(position);
        holder.tvTitle.setText(task.getTitle());
    }

    @Override
    public int getItemCount() {

        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;

        public TaskViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle); //item_task.xml
        }
    }

}
