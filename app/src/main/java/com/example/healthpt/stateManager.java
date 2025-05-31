package com.example.healthpt;

import android.content.Context;
import android.content.SharedPreferences;

public class stateManager {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SWITCH_STATE = "switch_state";

    private static stateManager instance;
    private SharedPreferences prefs;

    private stateManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized stateManager getInstance(Context context) {
        if (instance == null) {
            instance = new stateManager(context);
        }
        return instance;
    }

    public boolean isSwitchOn() {
        return prefs.getBoolean(KEY_SWITCH_STATE, false);
    }

    public void setSwitchState(boolean isOn) {
        prefs.edit().putBoolean(KEY_SWITCH_STATE, isOn).apply();
    }
}
