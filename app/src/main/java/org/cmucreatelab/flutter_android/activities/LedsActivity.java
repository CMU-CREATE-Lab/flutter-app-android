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
import org.cmucreatelab.flutter_android.ui.DialogFragmentChooseColor;
import org.cmucreatelab.flutter_android.ui.DialogFragmentColorHigh;
import org.cmucreatelab.flutter_android.ui.DialogFragmentHighValue;
import org.cmucreatelab.flutter_android.ui.DialogFragmentRelationship;
import org.cmucreatelab.flutter_android.ui.DialogFragmentSensor;

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


    private float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }


    private int[] hslToRgb(float h, float s, float l) {
        float r,g,b;

        if (s == 0f) {
            r = g = b = 1;
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        int[] result = {(int) (r * 255), (int) (g * 255), (int) (b * 255)};
        return result;
    }


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
