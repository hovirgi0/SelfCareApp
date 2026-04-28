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

public class StartPersonalizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
        // SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);

        //If the onboarding part is already done skip straight to main menu
        //When booting up the app only the main menu will open
        UserPreferences userPreferences = new UserPreferences(this);
        if (userPreferences.isOnboardingComplete()) {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_personalization_start);
    }

    public void onStartClicked(View view) {
        // empty
        Intent intent = new Intent(this, NamePersonalizationActivity.class);
        startActivity(intent);
    }
}

