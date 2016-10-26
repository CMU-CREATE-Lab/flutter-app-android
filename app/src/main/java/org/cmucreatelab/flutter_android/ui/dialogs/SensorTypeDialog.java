package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.sensors.AnalogOrUnknown;
import org.cmucreatelab.flutter_android.classes.sensors.BarometricPressure;
import org.cmucreatelab.flutter_android.classes.sensors.Distance;
import org.cmucreatelab.flutter_android.classes.sensors.Humidity;
import org.cmucreatelab.flutter_android.classes.sensors.Light;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoilMoisture;
import org.cmucreatelab.flutter_android.classes.sensors.Sound;
import org.cmucreatelab.flutter_android.classes.sensors.Temperature;
import org.cmucreatelab.flutter_android.classes.sensors.WindSpeed;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 8/22/2016.
 */
public class SensorTypeDialog extends DialogFragment implements View.OnClickListener {

    private String sensorText;
    private int portNumber;
    DialogSensorTypeListener sensorListener;


    private String getSensorText(int portNumber) {
        String result = "";

        switch (portNumber) {
            case 1:
                result = getString(R.string.sensor_port_1);
                break;
            case 2:
                result = getString(R.string.sensor_port_2);
                break;
            case 3:
                result = getString(R.string.sensor_port_3);
                break;
        }

        return result;
    }


    public static SensorTypeDialog newInstance(int portNumber, Serializable serializable) {
        SensorTypeDialog sensorTypeDialog = new SensorTypeDialog();

        Bundle args = new Bundle();
        args.putInt(Sensor.SENSOR_PORT_KEY, portNumber);
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        sensorTypeDialog.setArguments(args);

        return sensorTypeDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        portNumber = getArguments().getInt(Sensor.SENSOR_PORT_KEY);
        sensorText = getSensorText(portNumber);
        sensorListener = (DialogSensorTypeListener) getArguments().getSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_types, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(sensorText).setView(view);

        // bind click listeners
        view.findViewById(R.id.image_light).setOnClickListener(this);
        view.findViewById(R.id.image_soil_moisture).setOnClickListener(this);
        view.findViewById(R.id.image_distance).setOnClickListener(this);
        view.findViewById(R.id.image_sound).setOnClickListener(this);
        view.findViewById(R.id.image_wind_speed).setOnClickListener(this);
        view.findViewById(R.id.image_humidity).setOnClickListener(this);
        view.findViewById(R.id.image_temperature).setOnClickListener(this);
        view.findViewById(R.id.image_barometric_pressure).setOnClickListener(this);
        view.findViewById(R.id.image_analog_unknown).setOnClickListener(this);
        view.findViewById(R.id.image_no_sensor).setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        Sensor sensor = new NoSensor(portNumber);
        switch (view.getId()) {
            case R.id.image_light:
                Log.d(Constants.LOG_TAG, "onClickLightSensor");
                sensor = new Light(portNumber);
                break;
            case R.id.image_soil_moisture:
                Log.d(Constants.LOG_TAG, "onClickSoilMoistureSensor");
                sensor = new SoilMoisture(portNumber);
                break;
            case R.id.image_distance:
                Log.d(Constants.LOG_TAG, "onClickDistanceSensor");
                sensor = new Distance(portNumber);
                break;
            case R.id.image_sound:
                Log.d(Constants.LOG_TAG, "onClickSoundSensor");
                sensor = new Sound(portNumber);
                break;
            case R.id.image_wind_speed:
                Log.d(Constants.LOG_TAG, "onClickWindSpeedSensor");
                sensor = new WindSpeed(portNumber);
                break;
            case R.id.image_humidity:
                Log.d(Constants.LOG_TAG, "onClickHumiditySensor");
                sensor = new Humidity(portNumber);
                break;
            case R.id.image_temperature:
                Log.d(Constants.LOG_TAG, "onClickTemperatureSensor");
                sensor = new Temperature(portNumber);
                break;
            case R.id.image_barometric_pressure:
                Log.d(Constants.LOG_TAG, "onClickBarometricPressureSensor");
                sensor = new BarometricPressure(portNumber);
                break;
            case R.id.image_analog_unknown:
                Log.d(Constants.LOG_TAG, "onClickAnalogUnknownSensor");
                sensor = new AnalogOrUnknown(portNumber);
                break;
            case R.id.image_no_sensor:
                Log.d(Constants.LOG_TAG, "onClickNoSensor");
                sensor = new NoSensor(portNumber);
                break;
        }
        sensorListener.onSensorTypeChosen(sensor);
        this.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogSensorTypeListener {
        public void onSensorTypeChosen(Sensor sensor);
    }

}
