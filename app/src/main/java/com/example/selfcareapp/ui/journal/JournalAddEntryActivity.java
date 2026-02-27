package com.example.selfcareapp.ui.journal;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class JournalAddEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add_entry);
    }

    /**
     * Triggered by the "Bejegyzés mentése" (Save Entry) button.
     * Persists the emotional reflection.
     */
    public void onSaveEntryClicked(View view) {
        // Empty on purpose (Week 4 UI lock)
        // This will eventually save the text and mood to the database.
        finish(); // Returns to the list view after "saving"
    }
}