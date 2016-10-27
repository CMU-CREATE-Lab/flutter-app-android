package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
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
public abstract class BaseNavigationActivity extends AppCompatActivity {

    protected GlobalHandler globalHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalHandler = GlobalHandler.getInstance(this);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
      //  toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g));
    }


    @OnClick(R.id.text_sensors_menu)
    public void onClickSensorsMenu() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.text_robot_menu)
    public void onClickRobotMenu() {
        Log.d(Constants.LOG_TAG, "onClickRobotMenu");
        Intent intent = new Intent(this, RobotActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.text_data_log_menu)
    public void onClickDataLogMenu() {
        Log.d(Constants.LOG_TAG, "onClickDataLogMenu");
        Intent intent = new Intent(this, DataLogsActivity.class);
        startActivity(intent);
    }

}
