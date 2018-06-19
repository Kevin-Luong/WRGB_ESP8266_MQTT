package com.lhdkhanh.wrgb_esp8266_mqtt;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.flask.colorpicker.slider.LightnessSlider;
import com.flask.colorpicker.slider.OnValueChangedListener;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class Main4Activity_Controller002 extends Activity {

    /*
    Mapping the palette variables
     */
    RelativeLayout mainScreen;

    Button publishButton;

    SeekBar optionSeekBar;

    TextView optionValueTextView;

    EditText minOptionEditText;
    EditText maxOptionEditText;

    /*
    Create the global variables
     */
    static String MQTTHOST = "tcp://m11.cloudmqtt.com:13422";
    static String USERNAME = "ujzkugmb";
    static String PASSWORD = "JWmC8axGg_YC";
    String topicToPublishStr = "KEVIN/IN/Pathwayver1.0";
    String topicToSubscribeStr = "KEVIN/OUT/Pathwayver1.0";

    MqttAndroidClient client;

    private View root;
    private int stage1Color = 0xffff0000;

    private byte wwStage1 = 0;



    private int optionValue = 1;
    private int minOption = 0;
    private int maxOption = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);     // Hide the title
        setContentView(R.layout.activity_main4__controller002);

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


        optionSeekBar  = findViewById(R.id.optionSeekBar);

        minOptionEditText = findViewById(R.id.minOptionEditText);
        maxOptionEditText = findViewById(R.id.maxOptionEditText);

        optionValueTextView = findViewById(R.id.optionValueTextView);

        /*
        Mapping the value to the global variable
         */

        minOption = Integer.parseInt(minOptionEditText.getText().toString());
        maxOption = Integer.parseInt(maxOptionEditText.getText().toString());

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        /*
        Show the value onto screen
         */
        optionValueTextView.setText("Effect: " + 1);

        /*
        Connect the tasks with the relative actions
         */

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
                sendMessagebyMQTT();
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

        /*
        Connect to color picker table
        */
        ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);

        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override public void onColorChanged(int selectedColor) {
                // Handle on color change
                stage1Color = selectedColor;
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                stage1Color = selectedColor;
                sendMessagebyMQTT();
            }
        });


    }

    public void sendMessagebyMQTT(){
        if(isWiFiConnected()){

        }
        else{
            Toast.makeText(this, "Turn on WiFi please!", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(this, Integer.toHexString(stage1Color), Toast.LENGTH_SHORT).show();
                byte[] arayMessage = new byte[34];
                arayMessage[0]  = 'X';
                arayMessage[1]  = 'X';
                arayMessage[2]  = (byte) (stage1Color >> 16);
                arayMessage[3]  = (byte) (stage1Color >> 8);
                arayMessage[4]  = (byte) stage1Color;
                arayMessage[5]  = wwStage1;
                arayMessage[6]  = 0;
                arayMessage[7]  = 0;
                arayMessage[8]  = 0;
                arayMessage[9]  = 0;
                arayMessage[10] = 0;
                arayMessage[11] = 0;
                arayMessage[12] = 0;
                arayMessage[13] = 0;
                arayMessage[14] = 0;
                arayMessage[15] = 0;
                arayMessage[16] = 0;
                arayMessage[17] = 0;
                arayMessage[18] = 0;
                arayMessage[19] = 0;
                arayMessage[20] = 0;
                arayMessage[21] = 0;
                arayMessage[22] = 0;
                arayMessage[23] = 0;
                arayMessage[24] = 0;
                arayMessage[25] = 0;
                arayMessage[26] = 0;
                arayMessage[27] = 0;
                arayMessage[28] = 0;
                arayMessage[29] = 0;
                arayMessage[30] = 0;
                arayMessage[31] = 0;
                arayMessage[32] = 0;
                arayMessage[33] = (byte) optionValue;
                //Toast.makeText(Main3Activity_Controller001.this, "Message is publishing...", Toast.LENGTH_SHORT).show();
                try {
                    client.publish(topicToPublishStr, arayMessage, 0, false);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(Main4Activity_Controller002.this, "WiFi is turned off!\nPlease turn on Wifi then reconnect", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Connection to MQTT is lost/nPlease reconnect!", Toast.LENGTH_SHORT).show();
            connectToMQTT();
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
            Toast.makeText(Main4Activity_Controller002.this, "Connecting to " + MQTTHOST.toString(), Toast.LENGTH_SHORT).show();
            connectToMQTT();
        }
        else{
            Toast.makeText(Main4Activity_Controller002.this, "WiFi is turned off!\nPlease turn on Wifi then reconnect", Toast.LENGTH_SHORT).show();
        }
    }

    public void connectToMQTT(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Main4Activity_Controller002.this, "Connected!!", Toast.LENGTH_SHORT).show();
                    publishButton.setEnabled(true);
                    mqttSubscribe();
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            Toast.makeText(Main4Activity_Controller002.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Toast.makeText(Main4Activity_Controller002.this, message.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                            //Toast.makeText(Main4Activity_Controller002.this, "Delivery Complete!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Main4Activity_Controller002.this, "Connection Failed!!", Toast.LENGTH_SHORT).show();
                    publishButton.setEnabled(false);
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
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
