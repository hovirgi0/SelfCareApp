package com.example.selfcareapp.ui.todo;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ToDoListActivity extends BaseActivity {
    //Repository-t és az Adaptert osztályszintű változóba való kiemelése
    private TaskAdapter taskAdapter;
    private TaskRepository taskRepository;

    private RecyclerView rvTasks;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
        //SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //initialize DB and Adapter
        taskAdapter = new TaskAdapter();
        taskRepository = new TaskRepository(getApplication());

        //Setup RecycleView
        //local variable for activity_todo_list.xml-s ... other methods (refreshTaskList) cant see it need to declare it globally
        /*RecyclerView recyclerView = findViewById(R.id.rvTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(ToDoListActivity.this));
        recyclerView.setAdapter(taskAdapter); */

        //initialize the global variables (Finding the views in the layout)
        rvTasks = findViewById(R.id.rvTasks);
        tvEmpty = findViewById(R.id.tvEmptyState);

        //Setup RecyclerView
        // We use the global 'rvTasks' now instead of a local 'recyclerView'
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);

        // Setup Date and Time
        // finds the TextView by its ID
        TextView tvDateMetadata = findViewById(R.id.tvDateMetadata);
        // creates a formatter for date style
        // EEEE - day; MMM - month; d - day
        //Locale.getDefault() ensures that if the language of the emulator/device is to hungarian the date will also be displayed in hungarian
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE MMM d", java.util.Locale.getDefault());
        String currentDateAndTime = sdf.format(new java.util.Date());

        //sets the text
        tvDateMetadata.setText(currentDateAndTime);

        taskAdapter.setOnTaskStatusChangedListener(task -> {
                    new Thread(() -> {
                        taskRepository.editTask(task);
                    }).start();
        });

        // Rearrange
        taskAdapter.setOnTaskOrderChangedListener(reorderedTasks -> {
            new Thread(() -> {
                for (TaskEntity task : reorderedTasks) {
                    taskRepository.updateTask(task);
                }
            }).start();
        });


        //Log test 03.10.

        // Room doesnt allow db operations on the main thread
        /*new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            TaskDao taskDao = db.taskDao();

            // Create task
            TaskEntity task = new TaskEntity();
            task.setTitle("Buy groceries");
            task.setDescription("milk, eggs, bread");
            task.setCreatedAt(System.currentTimeMillis());
            task.setCompleted(false);
            task.userId = 1;

            // Insert task
            taskDao.insertTask(task);

            // Query tasks
            List<TaskEntity> tasks = taskDao.getTasksForUser(1);

            // Print to Logcat
            for (TaskEntity t : tasks) {
                Log.d("DB_TEST", "Task loaded: " + t.getTitle());
            }
        }).start(); */

        //Attach FAB click listener for flaoting add Button - using the id from the xml
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(this, ToDoAddEditActivity.class);
            startActivity(intent);
        });

        //setupSwipeToDelete();

        // setupSwipeToDelete(); helyett:
        setupTouchHelper();
    }
        //move
        // change it to be able to handle: swipe to delete (onSwiped) and rearrange - move (onMove)
       /*
        private void setupSwipeToDelete(){
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    TaskEntity taskToDelete = taskAdapter.getTaskAt(position);

                    new Thread(() -> {
                        taskRepository.deleteTask(taskToDelete); //delete task from database

                        // List<TaskEntity> tasks = taskRepository.getTasksForUser(1);

                        //dont need a new thread + db delete + telling db to fetch the new list + telling UI to update adapter
                        // since the same logic already gets executed in refreshTaskList()
                   // runOnUiThread(() -> {
                   //   taskAdapter.setTasks(tasks);
                   //   taskAdapter.notifyDataSetChanged();
                    });
                        // just call the method that handles ...
                        // This method already handles the Thread, the UI update, and the Empty State
                        refreshTaskList();
                    }).start();
                }
            }).attachToRecyclerView(rvTasks);
        }
        */

        private void setupTouchHelper() {
            ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                    // UP + DOWN engedélyezi a drag-et, LEFT + RIGHT a swipe törlést
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
            ) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    int from = viewHolder.getAdapterPosition();
                    int to = target.getAdapterPosition();
                    taskAdapter.moveItem(from, to);
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    TaskEntity taskToDelete = taskAdapter.getTaskAt(position);

                    new Thread(() -> {
                        taskRepository.deleteTask(taskToDelete);
                        refreshTaskList();
                    }).start();
                }
            };

            new ItemTouchHelper(callback).attachToRecyclerView(rvTasks);
        }

    @Override
    protected void onResume(){
        super.onResume();
        refreshTaskList();
    }

    private void refreshTaskList(){
        new Thread(() -> {
            List<TaskEntity> tasks = taskRepository.getTasksForUser(1);

            runOnUiThread(() -> {
                taskAdapter.setTasks(tasks);
                taskAdapter.notifyDataSetChanged();

                //empty state kezelése = ha nincs hozzáadva teendő
                TextView tvEmpty = findViewById(R.id.tvEmptyState);
                // task == null prevents the app from crashing if database hasnt returned anything
                //tasks.isEmpty() checks if theres any task added to database
                if (tasks == null || tasks.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE); //"Nincsenek még teendőid"
                    rvTasks.setVisibility(View.GONE); //
                } else {
                    tvEmpty.setVisibility(View.GONE); //Elrejtjük, ha van adat
                    rvTasks.setVisibility(View.VISIBLE); //Elrejtük magát a containert
                }
            });
        }).start();
    }

    /**
     * Triggered by the Floating Action Button (FAB).
     * Navigates to the Add/Edit screen.
     */
    //FAB is already used for deleting tasks from db - so onAddTaskClicked in the xml layout also isnt needed anymore
  /*  public void onAddTaskClicked(View view) {
        Intent intent = new Intent(this, ToDoAddEditActivity.class);
        startActivity(intent);
    } */
}