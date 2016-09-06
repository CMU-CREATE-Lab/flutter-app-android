package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.DialogFragmentHighValue;
import org.cmucreatelab.flutter_android.ui.DialogFragmentLowValue;
import org.cmucreatelab.flutter_android.ui.DialogFragmentRelationship;
import org.cmucreatelab.flutter_android.ui.DialogFragmentSensor;

import java.io.Serializable;

/**
 * Created by Steve on 8/31/2016.
 *
 * BaseServoLedActivity
 *
 * An abstract activity used for the extremely similar operations of the Servo and Led activities.
 *
 */
// TODO - use the seekbar as a slider
public abstract class BaseServoLedActivity extends BaseNavigationActivity implements    DialogFragmentRelationship.DialogRelationshipListener,
                                                                                        DialogFragmentSensor.DialogSensorListener,
                                                                                        DialogFragmentHighValue.DialogHighValueListener,
                                                                                        DialogFragmentLowValue.DialogLowValueListener,
                                                                                        Serializable {

    public static final String BASE_SERVO_LED_ACTIVITY_KEY = "base_servo_led_activity_key";
    private static final String HELP = "help";
    private static final String ADVANCED = "advanced";

    private ImageView selectedView;


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


    // listeners


    // TODO - keep track of the selected views so we can update them on selection
    public void onClickSelectSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectSensor");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        DialogFragmentSensor dialogFragmentSensor = DialogFragmentSensor.newInstance(this);
        dialogFragmentSensor.show(getSupportFragmentManager(), "tag");
    }


    public void onClickSelectRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectRelationship");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        DialogFragmentRelationship dialogFragmentRelationship = DialogFragmentRelationship.newInstance(this);
        dialogFragmentRelationship.show(getSupportFragmentManager(), "tag");
    }


    public void onClickSelectHighValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectHighValue");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        Log.d(Constants.LOG_TAG, view.toString());
    }


    public void onClickSelectLowValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectLowValue");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        Log.d(Constants.LOG_TAG, view.toString());
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen " + sensor.getClass().getSimpleName());
        this.selectedView.setImageResource(sensor.getSensorImageId());
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen " + relationship.getClass().getSimpleName());
        this.selectedView.setImageResource(relationship.getRelationshipImageId());
    }


    @Override
    public void onHighValueChosen(int highValue) {
        Log.d(Constants.LOG_TAG, "onHighValueChosen");
    }


    @Override
    public void onLowValueChosen(int lowValue) {
        Log.d(Constants.LOG_TAG, "onLowValueChosen");
    }
}
