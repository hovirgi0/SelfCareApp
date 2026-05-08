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
import com.example.selfcareapp.ui.todo.ToDoAddEditActivity;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for the To-Do list RecyclerView.
 * Manages task visualization, handles user interactions for task completion,
 * navigation to editing screens, and supports dynamic list reordering.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<TaskEntity> tasks;
    private setOnTaskStatusChangedListener taskStatusListener;
    private OnTaskOrderChangedListener orderChangedListener;

    public interface OnTaskOrderChangedListener {
        void onOrderChanged(List<TaskEntity> reorderedTasks);
    }

    public interface setOnTaskStatusChangedListener {
        void onTaskStatusChanged(TaskEntity task);
    }

    public void setOnTaskOrderChangedListener(OnTaskOrderChangedListener listener) {
        this.orderChangedListener = listener;
    }

    public void setOnTaskStatusChangedListener(setOnTaskStatusChangedListener taskStatuslistener) {
        this.taskStatusListener = taskStatuslistener;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    /**
     * Swaps task positions within the local list and updates their internal order.
     * Notifies the adapter for smooth UI animations and triggers the order change listener.
     */
    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(tasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).order = i;
        }

        if (orderChangedListener != null) {
            orderChangedListener.onOrderChanged(tasks);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity task = tasks.get(position);
        holder.tvTaskName.setText(task.getTaskName());

        // Reset listener to null before setting state to avoid unwanted trigger loops during binding
        holder.cbTaskDone.setOnCheckedChangeListener(null);
        holder.cbTaskDone.setChecked(task.isCompleted());

        holder.cbTaskDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            if (taskStatusListener != null) {
                taskStatusListener.onTaskStatusChanged(task);
            }
        });

        holder.btnEditTask.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ToDoAddEditActivity.class);
            intent.putExtra("TASK_ID", task.id);
            intent.putExtra("TASK_NAME", task.getTaskName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    /**
     * Retrieves the task entity at a specific adapter position.
     */
    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

    /**
     * ViewHolder for task items, providing direct access to UI components.
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbTaskDone;
        TextView tvTaskName;
        ImageButton btnEditTask;

        public TaskViewHolder(View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            btnEditTask = itemView.findViewById(R.id.btnEditTask);
            cbTaskDone = itemView.findViewById(R.id.cbTaskDone);
        }
    }
}