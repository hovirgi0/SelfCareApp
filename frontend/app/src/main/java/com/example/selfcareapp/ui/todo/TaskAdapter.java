package com.example.selfcareapp.ui.todo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;

import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<TaskEntity> tasks;
    private setOnTaskStatusChangedListener taskStatusListener;
    private OnTaskOrderChangedListener orderChangedListener;

    // Drag-to-reorder callback
    public interface OnTaskOrderChangedListener {
        void onOrderChanged(List<TaskEntity> reorderedTasks);
    }

    public void setOnTaskOrderChangedListener(OnTaskOrderChangedListener listener) {
        this.orderChangedListener = listener;
    }

    public interface setOnTaskStatusChangedListener {
        void onTaskStatusChanged(TaskEntity task);
    }

    //Task toggle box setter and getter
    public void setOnTaskStatusChangedListener(setOnTaskStatusChangedListener taskStatuslistener){
        this.taskStatusListener = taskStatuslistener;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    /**
     * Called by ItemTouchHelper when the user drags a task to a new position.
     * Swaps the items in the local list and notifies the adapter immediately
     * for smooth animation — the DB update happens in the Activity.
     */
    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(tasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        // Update the 'order' field to reflect new positions
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).order = i;
        }

        if (orderChangedListener != null) {
            orderChangedListener.onOrderChanged(tasks);
        }
    }

    @Override
    public @org.jspecify.annotations.NonNull TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull TaskViewHolder holder, int position){
        TaskEntity task = tasks.get(position);
        holder.tvTaskName.setText(task.getTaskName());

        //Todo toggle box
        //Phone state listener
        //set visual state - not to trigger a listener loop
        holder.cbTaskDone.setOnCheckedChangeListener(null);
        holder.cbTaskDone.setChecked(task.isCompleted());

        //when the user clicks on the box - listen for a new toggle
        holder.cbTaskDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked); //object data
            if (taskStatusListener != null){
                taskStatusListener.onTaskStatusChanged(task);        }
        });

        //Szerkesztés gomb kezelése
        holder.btnEditTask.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ToDoAddEditActivity.class);

            //Feladat átküldéséhez
            intent.putExtra("TASK_ID", task.id);
            intent.putExtra("TASK_NAME", task.getTaskName());
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

        CheckBox cbTaskDone;
        TextView tvTaskName;
        ImageButton btnEditTask;

        public TaskViewHolder(View itemView){
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName); //item_task.xml
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            cbTaskDone = itemView.findViewById(R.id.cbTaskDone);
        }
    }

    //Swipe-to-Delete törlés (alternatív megoldás - egyszerű, modern)
    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

}
