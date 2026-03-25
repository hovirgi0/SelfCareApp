package com.example.selfcareapp.ui.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class PreferencesPersonalizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_preferences);
    }

    public void onPreferencesContinueClicked(View view) {
        Intent intent = new Intent(this, CompletePersonalizationActivity.class);
        startActivity(intent);
    }
}