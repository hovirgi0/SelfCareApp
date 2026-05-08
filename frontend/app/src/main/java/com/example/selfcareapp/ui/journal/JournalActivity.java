package com.example.selfcareapp.ui.journal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.repository.JournalRepository;
import com.example.selfcareapp.ui.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Manages the display and management of user journal entries.
 * This activity handles listing entries from the local database,
 * providing an empty state view, and supporting swipe-to-delete functionality.
 */
public class JournalActivity extends BaseActivity {
    private JournalAdapter journalAdapter;
    private JournalRepository journalRepository;

    private RecyclerView rvJournalEntries;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        initializeComponents();
        setupRecyclerView();
        setupFab();
        setupSwipeToDelete();
    }

    private void initializeComponents() {
        journalAdapter = new JournalAdapter();
        journalRepository = new JournalRepository(getApplication());
        rvJournalEntries = findViewById(R.id.rvJournalEntries);
        tvEmpty = findViewById(R.id.tvEmptyState);
    }

    private void setupRecyclerView() {
        rvJournalEntries.setLayoutManager(new LinearLayoutManager(this));
        rvJournalEntries.setAdapter(journalAdapter);
    }

    private void setupFab() {
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddEntry);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(this, JournalAddEntryActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Configures an ItemTouchHelper to allow users to delete journal entries
     * by swiping left or right on a list item.
     */
    private void setupSwipeToDelete() {
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
                    refreshJournalEntries();
                }).start();
            }
        }).attachToRecyclerView(rvJournalEntries);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshJournalEntries();
    }

    /**
     * Asynchronously fetches journal entries for the current user and updates the UI.
     * Toggles between the list view and the empty state message based on data availability.
     */
    private void refreshJournalEntries() {
        new Thread(() -> {
            List<JournalEntryEntity> entries = journalRepository.getEntriesForUser(1);

            runOnUiThread(() -> {
                journalAdapter.setEntries(entries);
                journalAdapter.notifyDataSetChanged();

                if (entries == null || entries.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvJournalEntries.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    rvJournalEntries.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }
}