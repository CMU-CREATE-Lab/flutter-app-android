package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseFlutterActivity;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on dunno...it wasn't automatically generated. (I wonder what I did differently.)
 *
 * SensorsActivity
 *
 * An activity which handles the Sensors tab on the navigation bar.
 *
 */
public class SensorsActivity extends BaseFlutterActivity implements SensorTypeDialog.DialogSensorTypeListener, FlutterMessageListener, Serializable {

    public static final String SENSORS_ACTIVITY_KEY = "sensors_activity_key";

    // views
    private ImageView selectedView;
    private TextView currentSensorType;
    private TextView textSensor1Reading;
    private TextView textSensor2Reading;
    private TextView textSensor3Reading;
    private TextView currentHigh;
    private TextView currentLow;
    private ProgressBar progress1;
    private ProgressBar progress2;
    private ProgressBar progress3;

    private Timer timer;

    private Sensor[] sensors;
    private Sensor currentSensor;

    private boolean isPlayingSensors;


    private void updateDynamicViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSensor1Reading.setText(String.valueOf(sensors[0].getSensorReading()));
                progress1.setProgress(sensors[0].getSensorReading());
                textSensor2Reading.setText(String.valueOf(sensors[1].getSensorReading()));
                progress2.setProgress(sensors[1].getSensorReading());
                textSensor3Reading.setText(String.valueOf(sensors[2].getSensorReading()));
                progress3.setProgress(sensors[2].getSensorReading());

            }
        });
    }


    private void updateStaticViews() {
        if (sensors[0].getSensorType() != Sensor.Type.NO_SENSOR) {
            selectedView = (ImageView) findViewById(R.id.image_sensor_1);
            currentHigh = (TextView) findViewById(R.id.text_high_1);
            currentLow = (TextView) findViewById(R.id.text_low_1);
            currentSensorType = (TextView) findViewById(R.id.text_sensor_1);
            updateStaticByIndex(0);
        }

        if (sensors[1].getSensorType() != Sensor.Type.NO_SENSOR) {
            selectedView = (ImageView) findViewById(R.id.image_sensor_2);
            currentHigh = (TextView) findViewById(R.id.text_high_2);
            currentLow = (TextView) findViewById(R.id.text_low_2);
            currentSensorType = (TextView) findViewById(R.id.text_sensor_2);
            updateStaticByIndex(1);
        }

        if (sensors[2].getSensorType() != Sensor.Type.NO_SENSOR) {
            selectedView = (ImageView) findViewById(R.id.image_sensor_3);
            currentHigh = (TextView) findViewById(R.id.text_high_3);
            currentLow = (TextView) findViewById(R.id.text_low_3);
            currentSensorType = (TextView) findViewById(R.id.text_sensor_3);
            updateStaticByIndex(2);
        }
    }


    private void updateStaticByIndex(int index) {
        selectedView.setImageResource(sensors[index].getBlueImageId());
        currentHigh.setText(sensors[index].getHighTextId());
        currentLow.setText(sensors[index].getLowTextId());
        currentSensorType.setText(sensors[index].getSensorTypeId());
    }


    private void startSensorReading() {
        if (timer != null) {
            timer.cancel();
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                globalHandler.sessionHandler.addMessage(MessageConstructor.READ_SENSOR);
                globalHandler.sessionHandler.sendMessages();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 500);
    }


    private void stopSensorReading() {
        timer.cancel();
    }


    // Event Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_sensor));
        toolbar.setContentInsetsAbsolute(0,0);

        if (!globalHandler.sessionHandler.isBluetoothConnected) {
            NoFlutterConnectedDialog noFlutterConnectedDialog = NoFlutterConnectedDialog.newInstance(R.string.no_flutter_sensor);
            noFlutterConnectedDialog.setCancelable(false);
            noFlutterConnectedDialog.show(getSupportFragmentManager(), "tag");
        } else {
            String flutterName = globalHandler.sessionHandler.getFlutterName();
            if (flutterName != null && flutterName.length() > 0)
                toolbar.setTitle(flutterName);
            else
                toolbar.setTitle(R.string.unknown_device);
            isPlayingSensors = true;
            setSupportActionBar(toolbar);

            globalHandler.sessionHandler.setFlutterMessageListener(this);

            // init views
            textSensor1Reading = (TextView) findViewById(R.id.text_sensor_1_reading);
            textSensor2Reading = (TextView) findViewById(R.id.text_sensor_2_reading);
            textSensor3Reading = (TextView) findViewById(R.id.text_sensor_3_reading);
            progress1 = (ProgressBar) findViewById(R.id.progress_sensor_1);
            progress2 = (ProgressBar) findViewById(R.id.progress_sensor_2);
            progress3 = (ProgressBar) findViewById(R.id.progress_sensor_3);

            sensors = globalHandler.sessionHandler.getFlutter().getSensors();
            startSensorReading();
            updateDynamicViews();
            updateStaticViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        globalHandler.sessionHandler.setFlutterMessageListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (globalHandler.sessionHandler.isBluetoothConnected)
            stopSensorReading();
    }


    @Override
    public void onSensorTypeChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorTypeChosen");

        // find the index of the sensor chosen
        int index = -1;
        int i = 0;
        while(index == -1) {
            if (currentSensor.equals(sensors[i])) {
                index = i;
            }
            i++;
        }

        // update references
        sensors[index] = sensor;
        globalHandler.sessionHandler.getFlutter().setSensors(sensors);

        selectedView.setImageResource(sensor.getBlueImageId());
        currentSensorType.setText(getString(sensor.getSensorTypeId()));

        if (sensors[index].getSensorType() != Sensor.Type.NO_SENSOR) {
            currentHigh.setText(getString(sensor.getHighTextId()));
            currentLow.setText(getString(sensor.getLowTextId()));
        } else {
            currentHigh.setText("");
            currentLow.setText("");
        }
        updateDynamicViews();
    }


    @Override
    public void onMessageSent(String output) {
        Log.d(Constants.LOG_TAG, output);
        if (output.length() > 0 && !output.equals("OK") && !output.equals("FAIL")) {
            output = output.substring(2, output.length());
            String sensor1 = output.substring(0, output.indexOf(','));
            output = output.substring(output.indexOf(',')+1, output.length());
            String sensor2 = output.substring(0, output.indexOf(','));
            output = output.substring(output.indexOf(',')+1, output.length());
            String sensor3 = output;
            sensors[0].setSensorReading(Integer.valueOf(sensor1));
            sensors[1].setSensorReading(Integer.valueOf(sensor2));
            sensors[2].setSensorReading(Integer.valueOf(sensor3));
            updateDynamicViews();
        }
    }


    // Button Events


    @OnClick(R.id.image_sensor_1)
    public void onClickSensor1() {
        Log.d(Constants.LOG_TAG, "onClickSensor1");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_1);
        currentSensor = sensors[0];
        currentHigh = (TextView) findViewById(R.id.text_high_1);
        currentLow = (TextView) findViewById(R.id.text_low_1);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_1);
        SensorTypeDialog sensorTypeDialog = SensorTypeDialog.newInstance(1, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_2)
    public void onClickSensor2() {
        Log.d(Constants.LOG_TAG, "onClickSensor2");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_2);
        currentSensor = sensors[1];
        currentHigh = (TextView) findViewById(R.id.text_high_2);
        currentLow = (TextView) findViewById(R.id.text_low_2);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_2);
        SensorTypeDialog sensorTypeDialog = SensorTypeDialog.newInstance(2, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_3)
    public void onClickSensor3() {
        Log.d(Constants.LOG_TAG, "onClickSensor3");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_3);
        currentSensor = sensors[2];
        currentHigh = (TextView) findViewById(R.id.text_high_3);
        currentLow = (TextView) findViewById(R.id.text_low_3);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_3);
        SensorTypeDialog sensorTypeDialog = SensorTypeDialog.newInstance(3, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.button_play_pause)
    public void onClickPlayPause() {
        Log.d(Constants.LOG_TAG, "onClickPlayPause");
        Button button = (Button) findViewById(R.id.button_play_pause);
        if (isPlayingSensors) {
            button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.button_icon_play), null, null, null);
            button.setText(R.string.play_sensors);
            isPlayingSensors = false;
            stopSensorReading();
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.button_icon_pause), null, null, null);
            button.setText(R.string.pause_sensors);
            isPlayingSensors = true;
            startSensorReading();
        }
    }


    @OnClick(R.id.button_record)
    public void onClickRecordData() {
        Log.d(Constants.LOG_TAG, "onClickRecordData");
    }

}
