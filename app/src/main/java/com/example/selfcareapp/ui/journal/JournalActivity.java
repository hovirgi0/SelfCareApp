package com.example.selfcareapp.ui.journal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.dao.JournalDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.JournalRepository;
import com.example.selfcareapp.ui.todo.ToDoAddEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class JournalActivity extends AppCompatActivity {
    private JournalAdapter journalAdapter;
    private JournalRepository journalRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        journalAdapter = new JournalAdapter();
        journalRepository = new JournalRepository(getApplication());

        RecyclerView recyclerView = findViewById(R.id.rvJournalEntries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(journalAdapter);

        //Log test 03.16.
/*
        // Room doesnt allow db operations on the main thread
        new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            JournalDao journalDao = db.journalDao();

            // Create entry
            JournalEntryEntity entry = new JournalEntryEntity();
            entry.setTitle("Teszt bejegyzés");
            entry.setContent("Teszt bejegyzés szövege");
            entry.setDate(System.currentTimeMillis());
            entry.userId = 1;

            // Insert task
            journalDao.insertEntry(entry);

            // Query tasks
            List<JournalEntryEntity> entries = journalRepository.getEntriesForUser(1);

            // Print to Logcat
            for (JournalEntryEntity t : entries) {
                Log.d("DB_TEST", "Entry loaded: " + t.getTitle());
            }

            //Ui frissítése, hogy azonnal látható legyen
            runOnUiThread(() -> {
                journalAdapter.setEntries(entries);
                journalAdapter.notifyDataSetChanged();
            });
        }).start(); */

        //Initial load of data - without test text
        //refreshJournalEntries();

        //Attach FAB click listener for flaoting add Button
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddEntry);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(this, JournalAddEntryActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                JournalEntryEntity entryToDelete = journalAdapter.getEntryAt(position);

                new Thread(() -> {
                    journalRepository.deleteEntry(entryToDelete);

                    List<JournalEntryEntity> entries = journalRepository.getEntriesForUser(1);

                    runOnUiThread(() -> {
                        journalAdapter.setEntries(entries);
                        journalAdapter.notifyDataSetChanged();
                    });
                }).start();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshJournalEntries();
    }

    private void refreshJournalEntries(){
        new Thread(() -> {
            List<JournalEntryEntity> entries = journalRepository.getEntriesForUser(1);

            runOnUiThread(() -> {
                journalAdapter.setEntries(entries);
                journalAdapter.notifyDataSetChanged();

                //empty state kezelése
                TextView tvEmpty = findViewById(R.id.tvEmptyState);
                if (entries == null || entries.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE); //"Még nincsenek bejegyzéseid"
                } else {
                    tvEmpty.setVisibility(View.GONE); //"Elrejtjük, ha van adat
                }
            });
        }).start();
    }
    /**
     * Triggered by the Floating Action Button (FAB) [+].
     * Navigates to the Add Entry screen to start a new reflection.
     */
    public void onAddEntryClicked(View view) {
        // Intent to navigate to the Add Entry screen
        Intent intent = new Intent(this, JournalAddEntryActivity.class);
        startActivity(intent);
    }
}