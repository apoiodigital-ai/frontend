package com.example.apoiodigital.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigManager {

    private static final String PREFS_NAME = "app_prefs";
    private static final String FONT_SIZE_KEY = "font_size_option";

    private SharedPreferences sharedPreferences;

    public ConfigManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setFontSizeOption(String option) {
        sharedPreferences.edit().putString(FONT_SIZE_KEY, option).apply();
    }

    public String getFontSizeOption() {
        return sharedPreferences.getString(FONT_SIZE_KEY, "Médio");
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
