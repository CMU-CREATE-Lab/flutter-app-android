package org.cmucreatelab.flutter_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.OnClick;

/**
 * Created by Steve on 8/22/2016.
 */
public abstract class BaseNavigationActivity extends AppCompatActivity {

    protected GlobalHandler globalHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalHandler = GlobalHandler.newInstance(this);
    }

    @OnClick(R.id.image_connect_flutter)
    public void onClickConnectFlutter() {
        Log.d(Constants.LOG_TAG, "onClickConnectFlutter");
    }

    @OnClick(R.id.image_sensors)
    public void onClickSensors() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        if (globalHandler.sessionHandler.isBluetoothConnected) {
            Intent intent = new Intent(this, SensorsActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.image_servos)
    public void onClickServos() {
        Log.d(Constants.LOG_TAG, "onClickServos");
    }

    @OnClick(R.id.image_leds)
    public void onClickLeds() {
        Log.d(Constants.LOG_TAG, "onClickLeds");
    }

    @OnClick(R.id.image_speaker)
    public void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
    }

    @OnClick(R.id.image_data_logs)
    public void onClickDataLogs() {
        Log.d(Constants.LOG_TAG, "onClickDataLogs");
    }

}
