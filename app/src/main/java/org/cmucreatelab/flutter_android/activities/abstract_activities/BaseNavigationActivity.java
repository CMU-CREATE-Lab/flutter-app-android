package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.LedsActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.activities.ServosActivity;
import org.cmucreatelab.flutter_android.activities.SpeakerActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.OnClick;

/**
 * Created by Steve on 8/22/2016.
 *
 * BaseNavigationActivity
 *
 * An abstract activity used for the basic navigation bar on the bottom of most activities.
 *
 */
// TODO - add a field to keep track of which activity you are currently on, this way if you click your current tab nothing will happen.
    // TODO - I may change the base navigation to a toolbar now that I figured out how to use multiple toolbars in one activity.
public abstract class BaseNavigationActivity extends AppCompatActivity {

    protected GlobalHandler globalHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalHandler = GlobalHandler.newInstance(this);
    }

    /*@OnClick(R.id.image_connect_flutter)
    public void onClickConnectFlutter() {
        Log.d(Constants.LOG_TAG, "onClickConnectFlutter");
    }

    @OnClick(R.id.image_servos)
    public void onClickServos() {
        Log.d(Constants.LOG_TAG, "onClickServos");
        Intent intent = new Intent(this, ServosActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_leds)
    public void onClickLeds() {
        Log.d(Constants.LOG_TAG, "onClickLeds");
        Intent intent = new Intent(this, LedsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_speaker)
    public void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
        Intent intent = new Intent(this, SpeakerActivity.class);
        startActivity(intent);
    }*/

    @OnClick(R.id.image_sensors_menu)
    public void onClickSensorsMenu() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        if (globalHandler.sessionHandler.isBluetoothConnected) {
            Intent intent = new Intent(this, SensorsActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.image_robot_menu)
    public void onClickRobotMenu() {
        Log.d(Constants.LOG_TAG, "onClickRobotMenu");
        if (globalHandler.sessionHandler.isBluetoothConnected) {
            Intent intent = new Intent(this, RobotActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.image_data_log_menu)
    public void onClickDataLogMenu() {
        Log.d(Constants.LOG_TAG, "onClickDataLogMenu");
        Intent intent = new Intent(this, DataLogsActivity.class);
        startActivity(intent);
    }

}
