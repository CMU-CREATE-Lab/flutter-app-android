package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.RelationshipDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorDialog;

import java.io.Serializable;

/**
 * Created by Steve on 8/31/2016.
 *
 * BaseServoLedActivity
 *
 * An abstract activity used for the extremely similar operations of the Servo and Led activities.
 *
 */
public abstract class BaseServoLedActivity extends BaseNavigationActivity implements    RelationshipDialog.DialogRelationshipListener,
                                                                                        SensorDialog.DialogSensorListener,
                                                                                        Serializable {

    public static final String BASE_SERVO_LED_ACTIVITY_KEY = "base_servo_led_activity_key";
    private static final String HELP = "help";
    private static final String ADVANCED = "advanced";

    protected ImageView selectedView;
    protected Sensor[] currentSensors;


    protected Toolbar.OnMenuItemClickListener toolbarClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(Constants.LOG_TAG, "onClick " + item.toString());
            switch (item.toString()) {
                case HELP:
                    return false;
                case ADVANCED:
                    return false;
                default:
                    return false;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // onClickSelectSensor and onClickSelect Relationship are no different on LedsActivity or ServosActivity


    public void onClickSelectSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectSensor");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        SensorDialog sensorDialog = SensorDialog.newInstance(this);
        sensorDialog.show(getSupportFragmentManager(), "tag");
    }


    public void onClickSelectRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectRelationship");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        RelationshipDialog relationshipDialog = RelationshipDialog.newInstance(this);
        relationshipDialog.show(getSupportFragmentManager(), "tag");
    }

}
