package com.example.animalhub.Chat;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.animalhub.R;


public class    SettingsAPI {
    Context mContext;
    private SharedPreferences sharedSettings;

    public SettingsAPI(Context context) {
        mContext = context;
        sharedSettings = mContext.getSharedPreferences(mContext.getString(R.string.settings_file_name), Context.MODE_PRIVATE);
    }

    public String readSetting(String key) {
        return sharedSettings.getString(key, "na");
    }

    public void addUpdateSettings(String key, String value) {
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
