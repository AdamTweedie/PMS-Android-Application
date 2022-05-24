package com.deitel.pms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class User {

    protected final String user_id_prefs = "user_id";

    public void setUserId(Activity activity, String id) {
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences(user_id_prefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.apply();
    }

    public String getUserId(Activity activity) {
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences(user_id_prefs, Context.MODE_PRIVATE);
        final String id = sharedPreferences.getString("id", "No User Id");
        return id;
    }

    public void clearIdPreferences(Activity activity) {
        SharedPreferences userId = activity.getSharedPreferences(user_id_prefs,
                Context.MODE_PRIVATE);
        userId.edit().clear().commit();
    }

}
