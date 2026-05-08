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
/**
 * Finalizes the onboarding process.
 * Marks the session as completed and cleans up the navigation stack.
 */
public class CompletePersonalizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_complete);
    }

    /** 
     * Completes onboarding and redirects to Main Menu.
     * Uses Intent flags to prevent the user from going back to the onboarding screens.
     */
    public void onContinueClicked(View view) {
        UserPreferences userPreferences = new UserPreferences(this);
        userPreferences.setOnboardingComplete(true);

        Intent intent = new Intent(this, MainMenuActivity.class);

        // Security/UX: Clears the activity history so back button won't return here
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}