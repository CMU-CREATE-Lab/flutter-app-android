package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.DialogFragmentColorHigh;

import butterknife.ButterKnife;

/**
 * Created by Steve on 8/31/2016.
 *
 * LedsActivity
 *
 * An activity which handles the LEDs tab on the navigation bar.
 *
 */
public class LedsActivity extends BaseServoLedActivity implements DialogFragmentColorHigh.DialogHighColorListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leds);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // internal toolbars
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar1.setTitle(R.string.led_1);
        toolbar1.inflateMenu(R.menu.menu_servo_led);
        toolbar1.setOnMenuItemClickListener(toolbarClick);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitle(R.string.led_2);
        toolbar2.inflateMenu(R.menu.menu_servo_led);
        toolbar2.setOnMenuItemClickListener(toolbarClick);

        Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar3);
        toolbar3.setTitle(R.string.led_3);
        toolbar3.inflateMenu(R.menu.menu_servo_led);
        toolbar3.setOnMenuItemClickListener(toolbarClick);

        currentSensors = new NoSensor[3];
    }


    public void onClickSelectHighValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectHighValue");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
        DialogFragmentColorHigh dialogFragmentColorHigh = DialogFragmentColorHigh.newInstance(this);
        dialogFragmentColorHigh.show(getSupportFragmentManager(), "tag");
    }


    public void onClickSelectLowValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectLowValue");
        this.selectedView = (ImageView) ((ViewGroup) view).getChildAt(0);
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
    public void onHighColorChosen(int[] color) {
        // TODO
    }

}
