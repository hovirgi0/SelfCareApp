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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add_entry);

        etJournalEntry = findViewById(R.id.etJournalEntry);
        journalRepository = new JournalRepository(getApplication());
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
            Toast.makeText(this, "Kérlek írj valamit a naplódba", Toast.LENGTH_SHORT).show();
            return;
        }

        //JournalEntity összeállítása még csak itt lehet megadni
        JournalEntryEntity entry = new JournalEntryEntity();
        entry.content = content;
        entry.title = "Napi bejegyzés";
        entry.date = System.currentTimeMillis();
        entry.userId = 1;

        new Thread(() -> {
                journalRepository.insertEntry(entry);
            runOnUiThread(() ->  finish());
        }).start();
    }
}