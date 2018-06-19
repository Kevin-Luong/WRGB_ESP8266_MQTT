package com.lhdkhanh.wrgb_esp8266_mqtt;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.Nullable;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class Main3Activity_Controller001 extends Activity {

    /*
    Mapping the palette variables
     */
    RelativeLayout mainScreen;

    Button publishButton;
    ImageView colorPreview1;
    ImageView colorPreview2;
    ImageView colorPreview3;
    ImageView colorPreview4;
    ImageView colorPreview5;
    ImageView colorPreview6;

    SeekBar stepSeekBar;
    SeekBar speedSeekBar;
    SeekBar optionSeekBar;

    TextView stepValueTextView;
    TextView speedValueTextView;
    TextView optionValueTextView;

    EditText minStepEditText;
    EditText maxStepEditText;
    EditText minSpeedEditText;
    EditText maxSpeedEditText;
    EditText minOptionEditText;
    EditText maxOptionEditText;

    Switch   directionSwitch;

    /*
    Create the global variables
     */
    static String MQTTHOST = "tcp://m11.cloudmqtt.com:13422";
    static String USERNAME = "ujzkugmb";
    static String PASSWORD = "JWmC8axGg_YC";
    String topicToPublishStr = "KEVIN/IN/RGBWver1.0";
    String topicToSubscribeStr = "KEVIN/OUT/RGBWver1.0";

    MqttAndroidClient client;

    private int stage1Color = 0xffff0000;
    private int stage2Color = 0xff00ff00;
    private int stage3Color = 0xff0000ff;
    private int stage4Color = 0xffff0000;
    private int stage5Color = 0xff00ff00;
    private int stage6Color = 0xff0000ff;
    private byte wwStage1 = 0;
    private byte wwStage2 = 0;
    private byte wwStage3 = 0;
    private byte wwStage4 = 0;
    private byte wwStage5 = 0;
    private byte wwStage6 = 0;

    private int stepValue = 2;
    private int minStep = 2;
    private int maxStep = 64;
    private int speedValue = 150;
    private int minSpeed = 150;
    private int maxSpeed = 500;
    private int optionValue = 1;
    private int minOption = 1;
    private int maxOption = 10;

    private byte direction = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     // Hide the title
        setContentView(R.layout.activity_main3__controller001);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  // use this one to hide the on-screen navigator bar and status bar automatically


        /*
        Insert the background
         */
        mainScreen = findViewById(R.id.MainScreen);
        mainScreen.setBackgroundColor(0x30000080);
        //mainScreen.setBackgroundResource(R.drawable.internetbackground_06);

        /*
        Hide the status bar after creating the APP
         */
        hideStatusBar();

        /*
        Mapping the id to the palette variables
         */
        publishButton = findViewById(R.id.publishButton);
        colorPreview1 = findViewById(R.id.colorPreview1);
        colorPreview2 = findViewById(R.id.colorPreview2);
        colorPreview3 = findViewById(R.id.colorPreview3);
        colorPreview4 = findViewById(R.id.colorPreview4);
        colorPreview5 = findViewById(R.id.colorPreview5);
        colorPreview6 = findViewById(R.id.colorPreview6);

        stepSeekBar   = findViewById(R.id.stepSeekBar);
        speedSeekBar  = findViewById(R.id.speedSeekBar);
        optionSeekBar  = findViewById(R.id.optionSeekBar);

        minStepEditText = findViewById(R.id.minStepEditText);
        maxStepEditText = findViewById(R.id.maxStepEditText);
        minSpeedEditText = findViewById(R.id.minSpeedEditText);
        maxSpeedEditText = findViewById(R.id.maxSpeedEditText);
        minOptionEditText = findViewById(R.id.minOptionEditText);
        maxOptionEditText = findViewById(R.id.maxOptionEditText);

        directionSwitch = findViewById(R.id.directionSwitch);

        stepValueTextView = findViewById(R.id.stepValueTextView);
        speedValueTextView = findViewById(R.id.speedValueTextView);
        optionValueTextView = findViewById(R.id.optionValueTextView);

        /*
        Mapping the value to the global variable
         */
        colorPreview1.setColorFilter(stage1Color);
        colorPreview2.setColorFilter(stage2Color);
        colorPreview3.setColorFilter(stage3Color);
        colorPreview4.setColorFilter(stage4Color);
        colorPreview5.setColorFilter(stage5Color);
        colorPreview6.setColorFilter(stage6Color);

        minStep = Integer.parseInt(minStepEditText.getText().toString());
        maxStep = Integer.parseInt(maxStepEditText.getText().toString());
        minSpeed = Integer.parseInt(minSpeedEditText.getText().toString());
        maxSpeed = Integer.parseInt(maxSpeedEditText.getText().toString());
        minOption = Integer.parseInt(minOptionEditText.getText().toString());
        maxOption = Integer.parseInt(maxOptionEditText.getText().toString());

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        /*
        Show the value onto screen
         */
        stepValueTextView.setText("Fading speed: " + 2);
        speedValueTextView.setText("Signal speed: " + 150);
        optionValueTextView.setText("Effect: " + 1);

        /*
        Connect the tasks with the relative actions
         */
        stepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                stepValue = i*(maxStep - minStep)/stepSeekBar.getMax()+minStep;
                stepValueTextView.setText("Fading speed: " + stepValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speedValue = i*(maxSpeed - minSpeed)/speedSeekBar.getMax()+minSpeed;
                speedValueTextView.setText("Signal speed: " + speedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        optionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                optionValue = i*(maxOption - minOption)/optionSeekBar.getMax()+minOption;
                optionValueTextView.setText("Effect: " + optionValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        minStepEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    minStep = Integer.parseInt(minStepEditText.getText().toString());
                }
                catch(Throwable error){
                    minStep = 1;
                }
            }
        });

        maxStepEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    maxStep = Integer.parseInt(maxStepEditText.getText().toString());
                }
                catch(Throwable error){
                    maxStep = 1;
                }
            }
        });

        minSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    minSpeed = Integer.parseInt(minSpeedEditText.getText().toString());
                }
                catch(Throwable error){
                    minSpeed = 1;
                }
            }
        });

        maxSpeedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    maxSpeed = Integer.parseInt(maxSpeedEditText.getText().toString());
                }
                catch(Throwable error){
                    maxSpeed = 5;
                }
            }
        });

        minOptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    minOption = Integer.parseInt(minOptionEditText.getText().toString());
                }
                catch(Throwable error){
                    minOption =0;
                }
            }
        });

        maxOptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    maxOption = Integer.parseInt(maxOptionEditText.getText().toString());
                }
                catch(Throwable error){
                    maxOption = 10;
                }
            }
        });

        directionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)   direction = 1;
                else    direction = 0;
            }
        });

        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 1 color")
                        .initialColor(stage1Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage1Color = selectedColor;
                                colorPreview1.setColorFilter(stage1Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });


        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 2 color")
                        .initialColor(stage2Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage2Color = selectedColor;
                                colorPreview2.setColorFilter(stage2Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });


        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 3 color")
                        .initialColor(stage3Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage3Color = selectedColor;
                                colorPreview3.setColorFilter(stage3Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });


        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 4 color")
                        .initialColor(stage4Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage4Color = selectedColor;
                                colorPreview4.setColorFilter(stage4Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });


        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 5 color")
                        .initialColor(stage5Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage5Color = selectedColor;
                                colorPreview5.setColorFilter(stage5Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });


        /*
        Connect the color picker dialogs to the relative buttons
         */
        colorPreview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Main3Activity_Controller001.this;

                ColorPickerDialogBuilder

                        .with(context)
                        .setTitle("Choose stage 6 color")
                        .initialColor(stage6Color)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .lightnessSliderOnly()
                        .showColorPreview(true)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                stage6Color = selectedColor;
                                colorPreview6.setColorFilter(stage6Color);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Selected Color: 0x");
                                        sb.append(Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null);
                                        //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();  // Ex: Selected Color: 0xFFFF0000
                                }
                                hideStatusBar();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideStatusBar();
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Main3Activity_Controller001.this, android.R.color.holo_blue_dark))
                        .build()
                        .show();
                hideStatusBar();
            }
        });
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

    /*
    Hide the status bar when resuming the APP
     */
    public void onResume(){
        super.onResume();
        hideStatusBar();
    }

    public boolean isWiFiConnected() {
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /*
    Publish the message to ESP8266
     */
    public void mqttPublish(View w){
        if(client.isConnected()){
            if(isWiFiConnected()) {
                byte[] arayMessage = new byte[34];
                arayMessage[0] = 'X';
                arayMessage[1] = 'X';
                arayMessage[2] = (byte) (stage1Color >> 16);
                arayMessage[3] = (byte) (stage1Color >> 8);
                arayMessage[4] = (byte) stage1Color;
                arayMessage[5] = wwStage1;
                arayMessage[6] = (byte) (stage2Color >> 16);
                arayMessage[7] = (byte) (stage2Color >> 8);
                arayMessage[8] = (byte) stage2Color;
                arayMessage[9] = wwStage2;
                arayMessage[10] = (byte) (stage3Color >> 16);
                arayMessage[11] = (byte) (stage3Color >> 8);
                arayMessage[12] = (byte) stage3Color;
                arayMessage[13] = wwStage3;
                arayMessage[14] = (byte) (stage4Color >> 16);
                arayMessage[15] = (byte) (stage4Color >> 8);
                arayMessage[16] = (byte) stage4Color;
                arayMessage[17] = wwStage4;
                arayMessage[18] = (byte) (stage5Color >> 16);
                arayMessage[19] = (byte) (stage5Color >> 8);
                arayMessage[20] = (byte) stage5Color;
                arayMessage[21] = wwStage5;
                arayMessage[22] = (byte) (stage6Color >> 16);
                arayMessage[23] = (byte) (stage6Color >> 8);
                arayMessage[24] = (byte) stage6Color;
                arayMessage[25] = wwStage6;
                arayMessage[26] = direction;
                arayMessage[27] = (byte) (stepValue >> 8);
                arayMessage[28] = (byte) stepValue;
                arayMessage[29] = (byte) (speedValue >> 24);
                arayMessage[30] = (byte) (speedValue >> 16);
                arayMessage[31] = (byte) (speedValue >> 8);
                arayMessage[32] = (byte) speedValue;
                arayMessage[33] = (byte) optionValue;
                //Toast.makeText(Main3Activity_Controller001.this, "Message is publishing...", Toast.LENGTH_SHORT).show();
                try {
                    client.publish(topicToPublishStr, arayMessage, 0, false);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(Main3Activity_Controller001.this, "WiFi is turned off!\nPlease turn on Wifi then reconnect", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Connection to MQTT is lost/nPlease reconnect!", Toast.LENGTH_SHORT).show();
        }


    }

    /*

     */
    private void mqttSubscribe(){
        try{
            client.subscribe(topicToSubscribeStr, 0);
        }
        catch (MqttException e){
            e.printStackTrace();
        }
    }

    /*
    Connect the APP to MQTT Broker
     */
    public void mqttConnect(View w) {

        if(isWiFiConnected()) {
            Toast.makeText(Main3Activity_Controller001.this, "Connecting to " + MQTTHOST.toString(), Toast.LENGTH_SHORT).show();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            try {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Toast.makeText(Main3Activity_Controller001.this, "Connected!!", Toast.LENGTH_SHORT).show();
                        publishButton.setEnabled(true);
                        mqttSubscribe();
                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {
                                Toast.makeText(Main3Activity_Controller001.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                Toast.makeText(Main3Activity_Controller001.this, message.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {
                                //Toast.makeText(Main3Activity_Controller001.this, "Delivery Complete!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(Main3Activity_Controller001.this, "Connection Failed!!", Toast.LENGTH_SHORT).show();
                        publishButton.setEnabled(false);
                    }
                });
            }catch (MqttException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(Main3Activity_Controller001.this, "WiFi is turned off!\nPlease turn on Wifi then reconnect", Toast.LENGTH_SHORT).show();
        }


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

