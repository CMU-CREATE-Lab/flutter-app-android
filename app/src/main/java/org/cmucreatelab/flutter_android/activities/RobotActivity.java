package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.outputs.Led;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.LedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.SpeakerDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RobotActivity extends BaseNavigationActivity implements Serializable, FlutterMessageListener,
    ServoDialog.DialogServoListener,
    LedDialog.DialogLedListener {


    public static final String SERIALIZABLE_KEY = "serializable_key";

    private GlobalHandler globalHandler;
    private Servo[] servos;
    private Led[] leds;

    private boolean isSensorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_robot));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        globalHandler = GlobalHandler.getInstance(this);
        servos = globalHandler.sessionHandler.getFlutter().getServos();
        leds = globalHandler.sessionHandler.getFlutter().getLeds();
        if (!globalHandler.sessionHandler.isBluetoothConnected) {
            NoFlutterConnectedDialog noFlutterConnectedDialog = NoFlutterConnectedDialog.newInstance(R.string.no_flutter_robot);
            noFlutterConnectedDialog.setCancelable(false);
            noFlutterConnectedDialog.show(getSupportFragmentManager(), "tag");
        }
        isSensorData = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        globalHandler.sessionHandler.setFlutterMessageListener(this);
    }


    @Override
    public void onMessageSent(String output) {
        Log.d(Constants.LOG_TAG, "onMessageSent: " + output);
    }


    @Override
    public void onServoLinkCreated(String message) {
        Log.d(Constants.LOG_TAG, "onServoLinkCreated");
        globalHandler.sessionHandler.addMessage(message);
        globalHandler.sessionHandler.sendMessages();
    }


    @Override
    public void onLedLinkListener(ArrayList<String> msgs) {
        Log.d(Constants.LOG_TAG, "onLedLinkCreated");
        globalHandler.sessionHandler.addMessages(msgs);
        globalHandler.sessionHandler.sendMessages();
    }


    @OnClick(R.id.image_servo_1)
    public void onClickServo1() {
        Log.d(Constants.LOG_TAG, "onClickServo1");
        ServoDialog dialog = ServoDialog.newInstance(servos[0], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_servo_2)
    public void onClickServo2() {
        Log.d(Constants.LOG_TAG, "onClickServo2");
        ServoDialog dialog = ServoDialog.newInstance(servos[1], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_servo_3)
    public void onClickServo3() {
        Log.d(Constants.LOG_TAG, "onClickServo3");
        ServoDialog dialog = ServoDialog.newInstance(servos[2], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_led_1)
    public void onClickLed1() {
        Log.d(Constants.LOG_TAG, "onClickLed1");
        LedDialog dialog = LedDialog.newInstance(leds[0], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_led_2)
    public void onClickLed2() {
        Log.d(Constants.LOG_TAG, "onClickLed2");
        LedDialog dialog = LedDialog.newInstance(leds[1], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_led_3)
    public void onClickLed3() {
        Log.d(Constants.LOG_TAG, "onClickLed3");
        LedDialog dialog = LedDialog.newInstance(leds[2], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_speaker)
    public void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
        SpeakerDialog dialog = SpeakerDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.button_sensor_data)
    public void onClickSensorData() {
        Log.d(Constants.LOG_TAG, "onClickSensorData");
        if (!isSensorData) {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button));
            sensorData.setTextColor(Color.WHITE);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_white));
            simulateData.setTextColor(Color.BLACK);

            isSensorData = true;
            // TODO - update the sensor readings
        }
    }


    @OnClick(R.id.button_simulate_data)
    public void onClickSimulateData() {
        Log.d(Constants.LOG_TAG, "onclickSimulateData");
        if (isSensorData) {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_white));
            sensorData.setTextColor(Color.BLACK);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button));
            simulateData.setTextColor(Color.WHITE);

            isSensorData = false;
        }
    }

}
