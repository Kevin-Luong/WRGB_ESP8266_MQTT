package com.lhdkhanh.wrgb_esp8266_mqtt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity_ItemSelector extends AppCompatActivity {

    android.support.constraint.ConstraintLayout mainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__item_selector);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // use this one to hide the on-screen navigator bar and status bar automatically


        /*
        Insert the background
         */
        mainScreen = findViewById(R.id.PatternSelectorScreen);
        mainScreen.setBackgroundColor(0x30000080);


    }


    @Override     // use this one to hide the on-screen navigator bar and status bar automatically
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void device001Click(View w){
        Intent intent = new Intent(Main2Activity_ItemSelector.this, Main3Activity_Controller001.class);
        startActivity(intent);
    }

    public void device002Click(View w){
        Intent intent = new Intent(Main2Activity_ItemSelector.this, Main4Activity_Controller002.class);
        startActivity(intent);
    }
}
