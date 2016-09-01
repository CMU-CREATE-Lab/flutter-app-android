package org.cmucreatelab.flutter_android.ui;

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
public class DialogFragmentSensor extends DialogFragment implements View.OnClickListener {

    private String sensorText;
    DialogSensorListener sensorListener;


    public static DialogFragmentSensor newInstance(String sensor, Serializable serializable) {
        DialogFragmentSensor dialogFragmentSensor = new DialogFragmentSensor();

        Bundle args = new Bundle();
        args.putString(Sensor.SENSOR_KEY, sensor);
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        dialogFragmentSensor.setArguments(args);

        return dialogFragmentSensor;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sensorText = getArguments().getString(Sensor.SENSOR_KEY);
        sensorListener = (DialogSensorListener) getArguments().getSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensors, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.dialog_sensor) + " " + sensorText).setView(view);

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
        Sensor sensor = new NoSensor();
        switch (view.getId()) {
            case R.id.image_light:
                Log.d(Constants.LOG_TAG, "onClickLightSensor");
                sensor = new Light();
                break;
            case R.id.image_soil_moisture:
                Log.d(Constants.LOG_TAG, "onClickSoilMoistureSensor");
                sensor = new SoilMoisture();
                break;
            case R.id.image_distance:
                Log.d(Constants.LOG_TAG, "onClickDistanceSensor");
                sensor = new Distance();
                break;
            case R.id.image_sound:
                Log.d(Constants.LOG_TAG, "onClickSoundSensor");
                sensor = new Sound();
                break;
            case R.id.image_wind_speed:
                Log.d(Constants.LOG_TAG, "onClickWindSpeedSensor");
                sensor = new WindSpeed();
                break;
            case R.id.image_humidity:
                Log.d(Constants.LOG_TAG, "onClickHumiditySensor");
                sensor = new Humidity();
                break;
            case R.id.image_temperature:
                Log.d(Constants.LOG_TAG, "onClickTemperatureSensor");
                sensor = new Temperature();
                break;
            case R.id.image_barometric_pressure:
                Log.d(Constants.LOG_TAG, "onClickBarometricPressureSensor");
                sensor = new BarometricPressure();
                break;
            case R.id.image_analog_unknown:
                Log.d(Constants.LOG_TAG, "onClickAnalogUnknownSensor");
                sensor = new AnalogOrUnknown();
                break;
            case R.id.image_no_sensor:
                Log.d(Constants.LOG_TAG, "onClickNoSensor");
                sensor = new NoSensor();
                break;
        }
        sensorListener.onSensorChosen(sensor);
        this.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogSensorListener {
        public void onSensorChosen(Sensor sensor);
    }

}
