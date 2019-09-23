package com.example.drivescience;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    String TOKEN = "token";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public User(Context context) {
        preferences = context.getSharedPreferences(
                context.getString(R.string.user_preferences),
                context.MODE_PRIVATE
        );
        editor = preferences.edit();
    }

    public String getRootDriverToken() {
        return preferences.getString(TOKEN, null);
    }

    public boolean hasRootDriverToken() {
        return getRootDriverToken() != null && !getRootDriverToken().isEmpty();
    }

    public void setRootDriverToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }
}
