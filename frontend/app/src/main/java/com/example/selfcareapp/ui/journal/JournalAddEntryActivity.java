package com.example.selfcareapp.ui.journal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.repository.JournalRepository;
import com.example.selfcareapp.ui.BaseActivity;

/**
 * Provides functionality for users to compose new journal entries or edit existing ones.
 * This activity captures user reflections along with a selected mood and persists
 * the data to the local Room database.
 */
public class JournalAddEntryActivity extends BaseActivity {

    private EditText etJournalEntry;
    private JournalRepository journalRepository;

    private String selectedMoodEntry = "Neutral";
    private int currentEntryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add_entry);

        initializeViews();
        handleIncomingIntent();
    }

    private void initializeViews() {
        etJournalEntry = findViewById(R.id.etJournalEntry);
        journalRepository = new JournalRepository(getApplication());
    }

    /**
     * Checks if the activity was started with intent extras.
     * If an ENTRY_ID is present, the activity switches to "Edit Mode" and populates the UI.
     */
    private void handleIncomingIntent() {
        if (getIntent().hasExtra("ENTRY_ID")) {
            currentEntryId = getIntent().getIntExtra("ENTRY_ID", -1);
            etJournalEntry.setText(getIntent().getStringExtra("ENTRY_CONTENT"));
            selectedMoodEntry = getIntent().getStringExtra("ENTRY_MOOD");

            // Manually trigger the selection UI based on the loaded mood
            View viewToHighlight;
            switch (selectedMoodEntry) {
                case "Sad":
                    viewToHighlight = findViewById(R.id.moodSad);
                    break;
                case "Happy":
                    viewToHighlight = findViewById(R.id.moodHappy);
                    break;
                case "Great":
                    viewToHighlight = findViewById(R.id.moodGreat);
                    break;
                default:
                    viewToHighlight = findViewById(R.id.moodNeutral);
                    break;
            }
            if (viewToHighlight != null) {
                onMoodEntrySelected(viewToHighlight);
            }
        }
    }

    /**
     * Validates the user input and saves the entry.
     * Performs database operations on a background thread to ensure UI fluidity.
     */
    public void onSaveEntryClicked(View view) {
        String content = etJournalEntry.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Kérlek írj valamit!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            JournalEntryEntity entry = new JournalEntryEntity();
            entry.setMood(selectedMoodEntry);
            entry.setContent(content);
            entry.setDate(System.currentTimeMillis());
            entry.userId = 1;

            if (currentEntryId == -1) {
                journalRepository.insertEntry(entry);
            } else {
                entry.setId(currentEntryId);
                journalRepository.editEntry(entry);
            }
            runOnUiThread(() -> {
                Toast.makeText(JournalAddEntryActivity.this, "Bejegyzés sikeresen mentve", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    /**
     * Updates the selected mood state based on the tapped mood icon.
     * Highlights the selected icon and resets the others.
     * Triggered by the onClick attribute of the mood ImageViews in the XML.
     */
    public void onMoodEntrySelected(View view) {
        // Reset all icons to transparent
        findViewById(R.id.moodSad).setBackgroundColor(android.graphics.Color.TRANSPARENT);
        findViewById(R.id.moodNeutral).setBackgroundColor(android.graphics.Color.TRANSPARENT);
        findViewById(R.id.moodHappy).setBackgroundColor(android.graphics.Color.TRANSPARENT);
        findViewById(R.id.moodGreat).setBackgroundColor(android.graphics.Color.TRANSPARENT);

        // Highlight selected
        view.setBackgroundColor(
                com.google.android.material.color.MaterialColors.getColor(
                        view, com.google.android.material.R.attr.colorPrimaryContainer
                )
        );

        int id = view.getId();
        if (id == R.id.moodSad) {
            selectedMoodEntry = "Sad";
        } else if (id == R.id.moodNeutral) {
            selectedMoodEntry = "Neutral";
        } else if (id == R.id.moodHappy) {
            selectedMoodEntry = "Happy";
        } else if (id == R.id.moodGreat) {
            selectedMoodEntry = "Great";
        }
    }
}