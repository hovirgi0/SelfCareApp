package com.example.selfcareapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.selfcareapp.R;

/**
 * Base activity class that handles global theme and night mode settings.
 * All other activities inherit from this class to ensure visual consistency.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_THEME = "selected_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyThemeFromPrefs();
        super.onCreate(savedInstanceState);
    }

    /**
     * Reads the saved theme from SharedPreferences and applies the 
     * corresponding style and night mode before the UI is inflated.
     */
    private void applyThemeFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString(KEY_THEME, "soothing light");

        switch (theme) {
            case "dopamine bright":
                setTheme(R.style.Theme_SelfCareApp_Dopamine_Bright);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dopamine dark":
                setTheme(R.style.Theme_SelfCareApp_Dopamine_Dark);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "soothing dark":
                setTheme(R.style.Theme_SelfCareApp_Soothing_Dark);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                setTheme(R.style.Theme_SelfCareApp_Soothing_Light);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }
}