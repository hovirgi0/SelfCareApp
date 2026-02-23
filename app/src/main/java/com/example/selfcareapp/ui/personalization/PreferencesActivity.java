package com.example.selfcareapp.ui.personalization;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_preferences);
    }

    public void onPreferencesContinueClicked(View view) {
        // empty on purpose (Week 4 UI lock)
    }
}