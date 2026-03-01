package com.example.selfcareapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onBackClicked(View view) {
        finish();
    }

    public void onToneClicked(View view) {
        Log.d("SettingsActivity", "Hangnem kártya megnyomva!");
        // Week 4: UI only
    }

    public void onStyleClicked(View view) {
        // Week 4: UI only
    }

    public void onThemeClicked(View view) {
        // Week 4: UI only
    }
}
