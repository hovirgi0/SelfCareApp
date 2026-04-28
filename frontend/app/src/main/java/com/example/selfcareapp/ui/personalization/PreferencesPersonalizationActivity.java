package com.example.selfcareapp.ui.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;

public class PreferencesPersonalizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
       // SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_preferences);
    }

    public void onPreferencesContinueClicked(View view) {
        Intent intent = new Intent(this, CompletePersonalizationActivity.class);
        startActivity(intent);
    }
}