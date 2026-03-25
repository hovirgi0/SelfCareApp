package com.example.selfcareapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.selfcareapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_TONE = "selected_tone";
    private static final String KEY_STYLE = "selected_style";

    //Tone cards
    private LinearLayout cardToneNeutral, cardToneFriendly, cardToneCalm, cardToneEnergetic;

    //Style cards
    private LinearLayout cardStyleSupportive, cardStyleDirect;

    //Theme cards
    private LinearLayout cardThemeLight, cardThemeDark, cardThemeNatural, cardThemeMinimal;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Restore the theme before layout inflates - prevents visual flash -.-
        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        applyTheme(sharedPrefs.getString(KEY_THEME, "light")); //app theme will be 'light' by default

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindViews();
        loadSelections();
        setOnClicklisteners();
    }

    // Bind all card views by their XML id-ss
    private void bindViews() {
        //Tone cards
        cardToneNeutral = findViewById(R.id.cardToneNeutral);
        cardToneFriendly = findViewById(R.id.cardToneFriendly);
        cardToneCalm = findViewById(R.id.cardToneCalm);
        cardToneEnergetic = findViewById(R.id.cardToneEnergetic);

        //Style cards
        cardStyleSupportive = findViewById(R.id.cardStyleSupportive);
        cardStyleDirect = findViewById(R.id.cardStyleDirect);

        //Theme cards
        cardThemeLight = findViewById(R.id.cardThemeLight);
        cardThemeDark = findViewById(R.id.cardThemeDark);
        cardThemeNatural = findViewById(R.id.cardThemeNatural);
        cardThemeMinimal = findViewById(R.id.cardThemeMinimal);
    }

    // Highlight previously saved selections when screen opens
    private void loadSelections() {
        highlightTone(sharedPrefs.getString(KEY_TONE,"neutral"));
        highlightStyle(sharedPrefs.getString(KEY_STYLE, "supportive"));
        highlightTheme(sharedPrefs.getString(KEY_THEME, "light"));
    }

    // Attach click listeners to every card
    private void setOnClicklisteners() {
        cardToneNeutral.setOnClickListener(view -> selectTone("neutral"));
        cardToneFriendly.setOnClickListener(view -> selectTone("friendly"));
        cardToneCalm.setOnClickListener(view ->  selectTone("calm"));
        cardToneEnergetic.setOnClickListener(view -> selectTone("energetic"));

        cardStyleSupportive.setOnClickListener(view -> selectStyle("supportive"));
        cardStyleDirect.setOnClickListener(view -> selectStyle("direct"));

        cardThemeLight.setOnClickListener(view -> selectTheme("light"));
        cardThemeDark.setOnClickListener(view -> selectTheme("dark"));
        cardThemeNatural.setOnClickListener(view -> selectTheme("natural"));
        cardThemeMinimal.setOnClickListener(view -> selectTheme("minimal"));
    }

    private void selectTone(String tone) {
        sharedPrefs.edit().putString(KEY_TONE, tone).apply();
        highlightTone(tone);
    }

    private void selectStyle(String style) {
        sharedPrefs.edit().putString(KEY_STYLE, style).apply();
        highlightStyle(style);
    }

    private void selectTheme(String theme) {
        sharedPrefs.edit().putString(KEY_THEME, theme).apply();
        highlightTheme(theme);
    }

    private void highlightTone(String selected) {
        resetCard(cardToneNeutral);
        resetCard(cardToneFriendly);
        resetCard(cardToneCalm);
        resetCard(cardToneEnergetic);
        switch (selected) {
            case "neutral": activateCard(cardToneNeutral); break;
            case "friendly": activateCard(cardToneFriendly); break;
            case "calm": activateCard(cardToneCalm); break;
            case "energetic": activateCard(cardToneEnergetic); break;
        }
    }

    private void highlightStyle(String selected) {
        resetCard(cardStyleDirect);
        resetCard(cardStyleDirect);
        switch (selected) {
            case "supportive": activateCard(cardStyleSupportive); break;
            case "direct": activateCard(cardStyleDirect); break;
        }
    }

    private void highlightTheme(String selected) {
        resetCard(cardThemeLight);
        resetCard(cardThemeDark);
        resetCard(cardThemeNatural);
        resetCard(cardThemeMinimal);
        switch (selected) {
            case "light": activateCard(cardThemeLight); break;
            case "dark": activateCard(cardThemeDark); break;
            case "natural": activateCard(cardThemeNatural); break;
            case "minimal": activateCard(cardThemeMinimal); break;
        }
    }

    private void resetCard(LinearLayout card){
        if (card != null) card.setBackgroundColor(0xFFF2F2F2);
    }

    private void activateCard(LinearLayout card){
        if (card != null) card.setBackgroundColor(0xFFD0E8FF);
    }

    private void applyTheme(String theme){
        switch (theme) {
            // only hightlights the cards visually but doesnt switch the apps theme
            // theres alreay a method that does that
            /*case "light": activateCard(cardThemeLight); break;
            case "dark": activateCard(cardThemeDark); break;
            case "natural": activateCard(cardThemeNatural); break;
            case "minimal": activateCard(cardThemeMinimal); break;*/

                case "dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case "light":
                case "natural":
                case "minimal":
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
        }
    }

    public static void restoreTheme(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if ("dark".equals(prefs.getString(KEY_THEME, "light"))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void onBackClicked(View view) {
        finish();
    }
}
