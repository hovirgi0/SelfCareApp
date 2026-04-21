package com.example.selfcareapp.ui.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.MainMenuActivity;

public class CompletePersonalizationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_complete);
    }

    public void onContinueClicked(View view) {
        //mark onboarding as done so it never shows again
        UserPreferences userPreferences = new UserPreferences(this);
        userPreferences.setOnboardingComplete(true);

        Intent intent = new Intent(this, MainMenuActivity.class);

        //Clear the back stacks so back button doesnt return to onboarding
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
