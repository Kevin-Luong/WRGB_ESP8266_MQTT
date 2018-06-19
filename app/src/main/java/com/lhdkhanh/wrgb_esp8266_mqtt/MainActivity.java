package com.lhdkhanh.wrgb_esp8266_mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity{

    /*
    Mapping the palette variables
     */
    RelativeLayout mainScreen;

    private byte progressPercentage = 0;
    ProgressBar progressBar_loadingScreen;


    GestureDetector gestureDetector;
    int SWIPE_THRESHOLD = 300;
    int SWIPE_VELOCITY_THRESHOLD = 300;

    /*
    Actions will be done after executing the APP
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     // Hide the title
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // use this one to hide the on-screen navigator bar and status bar automatically



        /*
        Insert the background
         */
        mainScreen = findViewById(R.id.MainScreen);
        mainScreen.setBackgroundResource(R.drawable.ledup_loading_screen_background);

        /*
        Hide the status bar after creating the APP
         */
        hideStatusBar();

        gestureDetector = new GestureDetector(this, new MyGesture());


        progressBar_loadingScreen = findViewById(R.id.progressBar_loadingScreen);

        if(progressPercentage < 100) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (progressPercentage < 100) {
                        progressPercentage += 4;
                        progressBar_loadingScreen.setProgress(progressPercentage);

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    progressPercentage = 0;

                    mainScreen.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            gestureDetector.onTouchEvent(motionEvent);
                            return true;
                        }
                    });

                    Intent intent = new Intent(MainActivity.this, Main2Activity_ItemSelector.class);
                    startActivity(intent);
                }
            }).start();
        }
        else{
            Intent intent = new Intent(MainActivity.this, Main2Activity_ItemSelector.class);
            startActivity(intent);
        }
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


    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            if(e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){

            }
            else if(e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                Intent intent = new Intent(MainActivity.this, Main2Activity_ItemSelector.class);
                startActivity(intent);
            }
            else if(e2.getY() - e1.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){

            }
            else if(e1.getY() - e2.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                Intent intent = new Intent(MainActivity.this, Main2Activity_ItemSelector.class);
                startActivity(intent);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /*
    Hide the status bar when resuming the APP
     */
    public void onResume(){
        super.onResume();
        hideStatusBar();
    }



    /*
    Hiding status bar commands
     */
    private void hideStatusBar(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}

