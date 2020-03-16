package com.example.drivescience;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREFERENCES_KEY = "Root-demo-preferences";
    private static final String ACTIVE_DRIVER_ID_PREFERENCE = "activeDriverId";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public void setActiveDriverIdPreference(String driverId) {
        sharedPreferences.edit().putString(ACTIVE_DRIVER_ID_PREFERENCE, driverId).commit();
    }

    public String getActiveDriverIdPreference() {
        return sharedPreferences.getString(ACTIVE_DRIVER_ID_PREFERENCE, "");
    }
}
