package org.cmucreatelab.flutter_android.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.sensors.SensorFactory;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 8/22/2016.
 */
public class DialogSelectorFragment extends DialogFragment implements View.OnClickListener {

    private String sensorText;
    private Sensor sensor;
    DialogSensorListener sensorListener;


    public static DialogSelectorFragment newInstance(String sensor, Serializable serializable) {
        DialogSelectorFragment dialogSelectorFragment = new DialogSelectorFragment();

        Bundle args = new Bundle();
        args.putString(Sensor.SENSOR_KEY, sensor);
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        dialogSelectorFragment.setArguments(args);

        return dialogSelectorFragment;
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        sensorListener.onSensorChosen(sensor);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_light:
                Log.d(Constants.LOG_TAG, "onClickLightSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.LIGHT);
                this.dismiss();
                break;
            case R.id.image_soil_moisture:
                Log.d(Constants.LOG_TAG, "onClickSoilMoistureSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.SOIL_MOISTURE);
                this.dismiss();
                break;
            case R.id.image_distance:
                Log.d(Constants.LOG_TAG, "onClickDistanceSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.DISTANCE);
                this.dismiss();
                break;
            case R.id.image_sound:
                Log.d(Constants.LOG_TAG, "onClickSoundSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.SOUND);
                this.dismiss();
                break;
            case R.id.image_wind_speed:
                Log.d(Constants.LOG_TAG, "onClickWindSpeedSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.WIND_SPEED);
                this.dismiss();
                break;
            case R.id.image_humidity:
                Log.d(Constants.LOG_TAG, "onClickHumiditySensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.HUMIDITY);
                this.dismiss();
                break;
            case R.id.image_temperature:
                Log.d(Constants.LOG_TAG, "onClickTemperatureSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.TEMPERATURE);
                this.dismiss();
                break;
            case R.id.image_barometric_pressure:
                Log.d(Constants.LOG_TAG, "onClickBarometricPressureSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.BAROMETRIC_PRESSURE);
                this.dismiss();
                break;
            case R.id.image_analog_unknown:
                Log.d(Constants.LOG_TAG, "onClickAnalogUnknownSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.ANALOG_OR_UNKNOWN);
                this.dismiss();
                break;
            case R.id.image_no_sensor:
                Log.d(Constants.LOG_TAG, "onClickNoSensor");
                sensor = SensorFactory.getSensorType(Sensor.Type.NO_SENSOR);
                this.dismiss();
                break;
        }
    }


    // interface for an activity to listen for a choice
    public interface DialogSensorListener {
        public void onSensorChosen(Sensor sensor);
    }

}
