package com.example.selfcareapp.ui.todo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

        //Szerkesztés gomb kezelése
        holder.btnEditTask.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ToDoAddEditActivity.class);

            //Feladat átküldéséhez
            intent.putExtra("TASK_ID", task.id);
            intent.putExtra("TASK_TITLE", task.getTitle());
            intent.putExtra("TASK_DESCRIPTION", task.getDescription());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
       /* if (tasks == null) {
            return 0;
        }
        return tasks.size(); */
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageButton btnEditTask;

        public TaskViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle); //item_task.xml
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
        }
    }

    //Swipe-to-Delete törlés (alternatív megoldás - egyszerű, modern)
    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

}
