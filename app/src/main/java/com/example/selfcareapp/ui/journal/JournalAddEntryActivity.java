package com.example.selfcareapp.ui.journal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.repository.JournalRepository;

public class JournalAddEntryActivity extends AppCompatActivity {

    private EditText etJournalEntry;
    private JournalRepository journalRepository;

    private String selectedMoodEntry = "Neutral"; //This will be the default mood

    private int currentEntryId = -1; //-1 jelzi, ha új feladatról van szó
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add_entry);

        etJournalEntry = findViewById(R.id.etJournalEntry);
        journalRepository = new JournalRepository(getApplication());

        if (getIntent().hasExtra("Entry_ID")) {
            currentEntryId = getIntent().getIntExtra("TASK_ID", -1);
            etJournalEntry.setText(getIntent().getStringExtra("ENTRY_CONTENT"));
            selectedMoodEntry = getIntent().getStringExtra("ENTRY_MOOD");
        }
    }

    /**
     * Triggered by the "Bejegyzés mentése" (Save Entry) button.
     * Persists the emotional reflection.
     */
    public void onSaveEntryClicked(View view) {
        String content = etJournalEntry.getText().toString().trim();

        //Simple Validation Hibakezelés
        if(content.isEmpty()){
            etJournalEntry.setError("A bejegyzés nem maradhat üresen!"); //Vizuális hibaüzenet
            Toast.makeText(this, "Kérlek írj valamit!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
        //új hozzáadása
        JournalEntryEntity entry = new JournalEntryEntity();
        entry.setMood(selectedMoodEntry);
        //entry.setTitle(title);
        entry.setContent(content);
        entry.userId = 1;

            if (currentEntryId == -1) {
                //entry.setCreatedAt(System.currentTimeMillis());
                journalRepository.insertEntry(entry);

            } else {
                //Szerkesztés: létező task-et
                // Itt lehet lekérni a régit és módosítani v. újat küldeni ugyanazzal az id-val
                entry.setId(currentEntryId);
                journalRepository.editEntry(entry);
            }
            runOnUiThread(() ->  {
                //Validáció: Bejegyzés sikeresen hozzáadva
                Toast.makeText(JournalAddEntryActivity.this, "Bejegyzés sikeresen mentve", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    //Saving user's mood - in the xml layout in add entry there are for moods with and onMoodEntrySelected attribute
    public void onMoodEntrySelected(View view){
        //Sets the value and highlights the selected mood - gray for now
        view.setBackgroundColor(android.graphics.Color.LTGRAY);

        int id = view.getId();
        if (id == R.id.moodSad) selectedMoodEntry = "Sad";
        else if (id == R.id.moodNeutral) selectedMoodEntry = "Neutral";
        else if (id == R.id.moodHappy) selectedMoodEntry = "Happy";
        else if (id == R.id.moodGreat) selectedMoodEntry = "Great";

    }
}