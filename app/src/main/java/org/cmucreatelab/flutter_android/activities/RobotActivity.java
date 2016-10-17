package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RobotActivity extends BaseNavigationActivity {

    private GlobalHandler globalHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_robot));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        globalHandler = GlobalHandler.newInstance(this);
        if (!globalHandler.sessionHandler.isBluetoothConnected) {
            NoFlutterConnectedDialog noFlutterConnectedDialog = NoFlutterConnectedDialog.newInstance(R.string.no_flutter_robot);
            noFlutterConnectedDialog.setCancelable(false);
            noFlutterConnectedDialog.show(getSupportFragmentManager(), "tag");
        }
    }


    @OnClick(R.id.image_servo_1)
    public void onClickServo1() {
        Log.d(Constants.LOG_TAG, "onClickServo1");
    }


    @OnClick(R.id.image_servo_2)
    public void onClickServo2() {
        Log.d(Constants.LOG_TAG, "onClickServo2");
    }


    @OnClick(R.id.image_servo_3)
    public void onClickServo3() {
        Log.d(Constants.LOG_TAG, "onClickServo3");
    }


    @OnClick(R.id.image_led_1)
    public void onClickLed1() {
        Log.d(Constants.LOG_TAG, "onClickLed1");
    }


    @OnClick(R.id.image_led_2)
    public void onClickLed2() {
        Log.d(Constants.LOG_TAG, "onClickLed2");
    }


    @OnClick(R.id.image_led_3)
    public void onClickLed3() {
        Log.d(Constants.LOG_TAG, "onClickLed3");
    }


    @OnClick(R.id.image_speaker)
    public void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
    }

}
