package com.deitel.pms;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

public class ButtonUtils {

    /***
     * For buttons which are text only, change color onClick for 0.1 second.
     * @param v View - the button passed as an object.
     */
    public static void textButtonColorChange(View v) {
        v.setEnabled(true);
        Button button = (Button) v;
        button.setTextColor(Color.parseColor("#B185DB"));
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setTextColor(Color.parseColor("#6247AA"));
            }
        }, 250); // Millisecond 100 = 0.1 sec
    }

    public static void tabViewButtonColorChanger(View btnCurrent, View btnTarget) {
        btnCurrent.setEnabled(true);
        btnTarget.setEnabled(true);

        Button currentTabButton = (Button) btnCurrent;
        Button targetTabButton = (Button) btnTarget;

        currentTabButton.setTextColor(Color.parseColor("#B185DB"));
        targetTabButton.setTextColor(Color.parseColor("#6247AA"));

    }
}
