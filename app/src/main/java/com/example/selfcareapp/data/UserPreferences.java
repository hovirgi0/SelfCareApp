package com.example.selfcareapp.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREFS_NAME = "userPreferences";
    private static final String PREFS_USERNAME = "userName";
    private static final String PREFS_ONBOARDING_COMPLETE = "onboardingComplete";

    private final SharedPreferences prefs;

    public UserPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //save instead of set?
    public void savePrefsName(String name) {
        prefs.edit().putString(PREFS_USERNAME, name).apply();
    }

    public String getPrefsName() {
        return prefs.getString(PREFS_USERNAME, "");
    }

    public void setOnboardingComplete(boolean complete) {
        prefs.edit().putBoolean(PREFS_ONBOARDING_COMPLETE, complete).apply();
    }

    //not get - is ?
    public boolean isOnboardingComplete() {
        return prefs.getBoolean(PREFS_ONBOARDING_COMPLETE, false);
    }
}
