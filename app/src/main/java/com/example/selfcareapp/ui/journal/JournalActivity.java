package com.example.selfcareapp.ui.journal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class JournalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
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