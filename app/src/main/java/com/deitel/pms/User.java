package com.deitel.pms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class User {

    final String userEmail;

    public User (String email) {
        this.userEmail = email;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void saveEmailToSharedPref(Activity activity) {
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences("user_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", getUserEmail());
        editor.apply();
    }

    public String retrieveUserEmailFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPreferences = (SharedPreferences) activity
                .getSharedPreferences("user_id", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("id", "No User Id");
        return email;
}
}
