package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseFlutterActivity;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.DialogSelectorFragment;

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
public class SensorsActivity extends BaseFlutterActivity implements DialogSelectorFragment.DialogSensorListener, FlutterConnectListener, FlutterMessageListener, Serializable {

    public static final String SENSORS_ACTIVITY_KEY = "sensors_activity_key";

    // views
    private ImageView imageSensor1;
    private ImageView imageSensor2;
    private ImageView imageSensor3;
    private TextView textSensor1;
    private TextView textSensor2;
    private TextView textSensor3;
    private TextView textSensor1Reading;
    private TextView textSensor2Reading;
    private TextView textSensor3Reading;
    private AlertDialog connectingDialog;
    private AlertDialog.Builder builder;

    private Timer timer;

    private DialogSelectorFragment dialogSelectorFragment;

    private Sensor[] sensors;
    private Sensor currentSensor;

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
        timer.schedule(timerTask, 0, 500);
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
        setSupportActionBar(mainToolbar);

        globalHandler.sessionHandler.setFlutterConnectListener(this);
        globalHandler.sessionHandler.setFlutterMessageListener(this);

        timer = new Timer();

        // init views
        imageSensor1 = (ImageView) findViewById(R.id.image_sensor_1);
        imageSensor2 = (ImageView) findViewById(R.id.image_sensor_2);
        imageSensor3 = (ImageView) findViewById(R.id.image_sensor_3);
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


    @OnClick(R.id.image_sensor_1)
    public void onClickSensor1() {
        Log.d(Constants.LOG_TAG, "onClickSensor1");
        currentSensor = sensors[0];
        dialogSelectorFragment = DialogSelectorFragment.newInstance("Sensor Port 1?", this);
        dialogSelectorFragment.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_2)
    public void onClickSensor2() {
        Log.d(Constants.LOG_TAG, "onClickSensor2");
        currentSensor = sensors[1];
        dialogSelectorFragment = DialogSelectorFragment.newInstance("Sensor Port 2?", this);
        dialogSelectorFragment.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.image_sensor_3)
    public void onClickSensor3() {
        Log.d(Constants.LOG_TAG, "onClickSensor3");
        currentSensor = sensors[2];
        dialogSelectorFragment = DialogSelectorFragment.newInstance("Sensor Port 3?", this);
        dialogSelectorFragment.show(getSupportFragmentManager(), "tag");
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen");
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
        updateViews();

        // TODO - show correct view
        switch (sensors[index].getSensorType()) {
            case LIGHT:
                startSensorReading();
                break;
            case SOIL_MOISTURE:
                startSensorReading();
                break;
            case DISTANCE:
                startSensorReading();
                break;
            case SOUND:
                startSensorReading();
                break;
            case WIND_SPEED:
                startSensorReading();
                break;
            case HUMIDITY:
                startSensorReading();
                break;
            case TEMPERATURE:
                startSensorReading();
                break;
            case BAROMETRIC_PRESSURE:
                startSensorReading();
                break;
            case ANALOG_OR_UNKNOWN:
                startSensorReading();
                break;
            case NO_SENSOR:
                break;
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
