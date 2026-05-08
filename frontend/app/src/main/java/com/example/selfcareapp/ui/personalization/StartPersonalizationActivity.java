package com.example.selfcareapp.ui.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.MainMenuActivity;
import com.example.selfcareapp.ui.SettingsActivity;
import com.example.selfcareapp.ui.todo.ToDoListActivity;

/**
 * Entry point of the application.
 * Acts as a router: redirects to the Main Menu if onboarding is already finished,
 * otherwise displays the welcome screen.
 */
public class StartPersonalizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserPreferences userPreferences = new UserPreferences(this);
        
        // State check: If onboarding is complete, skip this flow entirely
        if (userPreferences.isOnboardingComplete()) {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish(); // Close this activity so it's removed from the back stack
            return;
        }

        setContentView(R.layout.activity_personalization_start);
    }

    /** Navigates to the name input screen. */
    public void onStartClicked(View view) {
        startActivity(new Intent(this, NamePersonalizationActivity.class));
    }
}