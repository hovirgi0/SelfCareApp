package com.example.selfcareapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.selfcareapp.R;

public class SettingsActivity extends BaseActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_TONE = "selected_tone";
    private static final String KEY_STYLE = "selected_style";

    //Tone cards
    private LinearLayout cardToneNeutral, cardToneFriendly, cardToneCalm, cardToneEnergetic;

    //Style cards
    private LinearLayout cardStyleSupportive, cardStyleDirect;

    //Theme cards
    private LinearLayout cardThemeDopamineBright, cardThemeDopamineDark, cardThemeSoothingLight, cardThemeSoothingDark;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Restore the theme before layout inflates - prevents visual flash -.-
        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedTheme = sharedPrefs.getString(KEY_THEME, "soothing light"); //app theme will be 'light' by default

        //setTheme()
        switch (selectedTheme) {
            case "dopamine bright": setTheme(R.style.Theme_SelfCareApp_Dopamine_Bright); break;
            case "dopamine dark":   setTheme(R.style.Theme_SelfCareApp_Dopamine_Dark);   break;
            case "soothing dark":   setTheme(R.style.Theme_SelfCareApp_Soothing_Light);   break;
            default:                setTheme(R.style.Theme_SelfCareApp_Soothing_Dark);  break;
        }

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
        cardThemeDopamineBright = findViewById(R.id.cardThemeDopamineBright);
        cardThemeDopamineDark = findViewById(R.id.cardThemeDopamineDark);
        cardThemeSoothingLight = findViewById(R.id.cardThemeSoothingLight);
        cardThemeSoothingDark = findViewById(R.id.cardThemeSoothingDark);
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

        cardThemeDopamineBright.setOnClickListener(view -> selectTheme("dopamine bright"));
        cardThemeDopamineDark.setOnClickListener(view -> selectTheme("dopamine dark"));
        cardThemeSoothingLight.setOnClickListener(view -> selectTheme("soothing light"));
        cardThemeSoothingDark.setOnClickListener(view -> selectTheme("soothing dark"));
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
        applyTheme(theme); //apply the logic

        recreate(); //force the activity to restart to show the new theme immediately
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
        resetCard(cardStyleSupportive);
        resetCard(cardStyleDirect);
        switch (selected) {
            case "supportive": activateCard(cardStyleSupportive); break;
            case "direct": activateCard(cardStyleDirect); break;
        }
    }

    private void highlightTheme(String selected) {
        resetCard(cardThemeDopamineBright);
        resetCard(cardThemeDopamineDark);
        resetCard(cardThemeSoothingLight);
        resetCard(cardThemeSoothingDark);
        switch (selected) {
            case "dopamine bright": activateCard(cardThemeDopamineBright); break;
            case "dopamine dark": activateCard(cardThemeDopamineDark); break;
            case "soothing light": activateCard(cardThemeSoothingLight); break;
            case "soothing dark": activateCard(cardThemeSoothingDark); break;
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

            case "soothing dark":
            case "dopamine dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

        public static void restoreTheme(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString(KEY_THEME, "soothing light");
        if ("soothing dark".equals(theme) || "dopamine dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void onBackClicked(View view) {
        finish();
    }
}
