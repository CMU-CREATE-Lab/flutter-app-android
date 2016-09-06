package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseFlutterActivity;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.DialogFragmentSensorType;

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
public class SensorsActivity extends BaseFlutterActivity implements DialogFragmentSensorType.DialogSensorTypeListener, FlutterConnectListener, FlutterMessageListener, Serializable {

    public static final String SENSORS_ACTIVITY_KEY = "sensors_activity_key";

    // views
    private ImageView selectedView;
    private TextView textSensor1;
    private TextView textSensor2;
    private TextView textSensor3;
    private TextView textSensor1Reading;
    private TextView textSensor2Reading;
    private TextView textSensor3Reading;
    private AlertDialog connectingDialog;
    private AlertDialog.Builder builder;

    private Timer timer;

    private Sensor[] sensors;
    private Sensor currentSensor;

    private boolean isPlayingSensors;


    private void updateViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sensors[0].getSensorType() != Sensor.Type.NO_SENSOR) {
                    textSensor1.setText(sensors[0].getSensorType().toString());
                    textSensor1Reading.setText(String.valueOf(sensors[0].getSensorReading()));
                }
                else{
                    textSensor1.setText(R.string.select_sensor);
                    textSensor1Reading.setText("");
                }
                if (sensors[1].getSensorType() != Sensor.Type.NO_SENSOR) {
                    textSensor2.setText(sensors[1].getSensorType().toString());
                    textSensor2Reading.setText(String.valueOf(sensors[1].getSensorReading()));
                }
                else{
                    textSensor2.setText(R.string.select_sensor);
                    textSensor2Reading.setText("");
                }
                if (sensors[2].getSensorType() != Sensor.Type.NO_SENSOR) {
                    textSensor3.setText(sensors[2].getSensorType().toString());
                    textSensor3Reading.setText(String.valueOf(sensors[2].getSensorReading()));
                }
                else {
                    textSensor3.setText(R.string.select_sensor);
                    textSensor3Reading.setText("");
                }
            }
        });
    }


    private void startSensorReading() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                globalHandler.sessionHandler.setMessageInput(MessageConstructor.READ_SENSOR);
                globalHandler.sessionHandler.sendMessage();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 500);
    }


    private void stopSensorReading() {
        timer.cancel();
    }


    private void onClickPlaySensors() {
        Log.d(Constants.LOG_TAG, "onClickPlaySensors");
        isPlayingSensors = true;
        startSensorReading();
        invalidateOptionsMenu();
    }


    private void onClickPauseSensors() {
        Log.d(Constants.LOG_TAG, "onClickPauseSensors");
        isPlayingSensors = false;
        stopSensorReading();
        invalidateOptionsMenu();
    }


    private void onClickRecordData() {
        Log.d(Constants.LOG_TAG, "onClickRecordData");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        ButterKnife.bind(this);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        String flutterName = globalHandler.sessionHandler.getFlutterName();
        if (flutterName != null && flutterName.length() > 0)
            mainToolbar.setTitle(flutterName);
        else
            mainToolbar.setTitle(R.string.unknown_device);
        isPlayingSensors = true;
        setSupportActionBar(mainToolbar);

        globalHandler.sessionHandler.setFlutterConnectListener(this);
        globalHandler.sessionHandler.setFlutterMessageListener(this);

        // init views
        textSensor1 = (TextView) findViewById(R.id.text_sensor_1);
        textSensor2 = (TextView) findViewById(R.id.text_sensor_2);
        textSensor3 = (TextView) findViewById(R.id.text_sensor_3);
        textSensor1Reading = (TextView) findViewById(R.id.text_sensor_1_reading);
        textSensor2Reading = (TextView) findViewById(R.id.text_sensor_2_reading);
        textSensor3Reading = (TextView) findViewById(R.id.text_sensor_3_reading);

        sensors = globalHandler.sessionHandler.getFlutter().getSensors();
        updateViews();

        if (!globalHandler.sessionHandler.isBluetoothConnected) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
            builder.setMessage(String.format("Connecting to:\n%s\n\n(%s)", flutterName, "If it is taking awhile, click the search button to make the flutter start searching again."));
            builder.setTitle(R.string.app_name);
            connectingDialog = builder.create();
            connectingDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sensors, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isPlayingSensors) {
            menu.findItem(R.id.item_play_sensors).setVisible(true);
            menu.findItem(R.id.item_pause_sensors).setVisible(false);
        } else {
            menu.findItem(R.id.item_play_sensors).setVisible(false);
            menu.findItem(R.id.item_pause_sensors).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_play_sensors:
                onClickPlaySensors();
                return true;
            case R.id.item_pause_sensors:
                onClickPauseSensors();
                return true;
            case R.id.item_record_data:
                onClickRecordData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.image_sensor_1)
    public void onClickSensor1() {
        Log.d(Constants.LOG_TAG, "onClickSensor1");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_1);
        currentSensor = sensors[0];
        DialogFragmentSensorType dialogFragmentSensorType = DialogFragmentSensorType.newInstance("Sensor Port 1?", this);
        dialogFragmentSensorType.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_2)
    public void onClickSensor2() {
        Log.d(Constants.LOG_TAG, "onClickSensor2");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_1);
        currentSensor = sensors[1];
        DialogFragmentSensorType dialogFragmentSensorType = DialogFragmentSensorType.newInstance("Sensor Port 2?", this);
        dialogFragmentSensorType.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_3)
    public void onClickSensor3() {
        Log.d(Constants.LOG_TAG, "onClickSensor3");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_3);
        currentSensor = sensors[2];
        DialogFragmentSensorType dialogFragmentSensorType = DialogFragmentSensorType.newInstance("Sensor Port 3?", this);
        dialogFragmentSensorType.show(getSupportFragmentManager(), "tag");
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

        selectedView.setImageResource(sensor.getSensorImageId());
        updateViews();

        if (sensors[index].getSensorType() != Sensor.Type.NO_SENSOR) {
            startSensorReading();
        }
    }


    @Override
    public void onMessageSent(String output) {
        Log.d(Constants.LOG_TAG, "onMessageSent");
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
            updateViews();
        }
    }


    @Override
    public void onConnected(boolean connected) {
        Log.d(Constants.LOG_TAG, "Is connected: " + connected);
        if (connected && connectingDialog != null  && connectingDialog.isShowing()) {
            connectingDialog.dismiss();
        }
    }

}
