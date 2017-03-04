package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.FlutterStatusActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.FlutterStatusDialog;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Steve on 8/22/2016.
 *
 * BaseNavigationActivity
 *
 * An abstract activity used for the basic navigation bar on the bottom of most activities.
 *
 */
// TODO - add a field to keep track of which activity you are currently on, this way if you click your current tab nothing will happen.
public abstract class BaseNavigationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "onCreate - " + getClass() );
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "onResume - " + getClass() );
        GlobalHandler.getInstance(getApplicationContext()).sessionHandler.setCurrentActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.LOG_TAG, "onPause - " + getClass() );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, "onDestroy - " + getClass() );
    }


    @Optional @OnClick(R.id.text_menu_sensor)
    public void onClickSensorsMenu() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }


    @Optional @OnClick(R.id.text_menu_robot)
    public void onClickRobotMenu() {
        Log.d(Constants.LOG_TAG, "onClickRobotMenu");
        Intent intent = new Intent(this, RobotActivity.class);
        startActivity(intent);
    }


    @Optional @OnClick(R.id.text_menu_datalog)
    public void onClickDataLogMenu() {
        Log.d(Constants.LOG_TAG, "onClickDataLogMenu");
        Intent intent = new Intent(this, DataLogsActivity.class);
        startActivity(intent);
    }

    @Optional @OnClick(R.id.image_flutter_status_button)
    public void onClickFlutterStatus() {
        Log.d(Constants.LOG_TAG, "onClickFlutterStatus");
        FlutterStatusDialog.displayDialog(this, 0);
    }

}
