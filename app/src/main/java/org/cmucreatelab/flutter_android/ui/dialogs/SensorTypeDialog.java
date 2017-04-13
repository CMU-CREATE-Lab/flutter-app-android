package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.classes.sensors.AnalogOrUnknownSensor;
import org.cmucreatelab.flutter_android.classes.sensors.BarometricPressureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.DistanceSensor;
import org.cmucreatelab.flutter_android.classes.sensors.HumiditySensor;
import org.cmucreatelab.flutter_android.classes.sensors.LightSensor;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoilMoistureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoundSensor;
import org.cmucreatelab.flutter_android.classes.sensors.TemperatureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.AirQualitySensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 8/22/2016.
 *
 * SensorTypeDialog
 *
 * A Dialog that prompts the user to choose what kind of sensors are in the ports. (SensorsActivity)
 */
public abstract class SensorTypeDialog extends DialogFragment implements View.OnClickListener {

    private String sensorText;
    private int portNumber;
    DialogSensorTypeListener sensorListener;

    protected ImageView imageLight;
    protected ImageView imageSoilMoisture;
    protected ImageView imageDistance;
    protected ImageView imageSound;
    protected ImageView imageAirQuality;
    protected ImageView imageHumidity;
    protected ImageView imageTemperature;
    protected ImageView imageBarometricPressure;
    protected ImageView imageAnalogUnkown;
    protected ImageView imageNoSensor;


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


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        portNumber = getArguments().getInt(Constants.SerializableKeys.SENSOR_PORT_KEY);
        sensorText = getSensorText(portNumber);
        sensorListener = (DialogSensorTypeListener) getArguments().getSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_types, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_sensor_type_dialog_title)).setText(getSensorText(portNumber));

        // bind click listeners
        imageLight = (ImageView) view.findViewById(R.id.image_light);
        imageSoilMoisture = (ImageView) view.findViewById(R.id.image_soil_moisture);
        imageDistance = (ImageView) view.findViewById(R.id.image_distance);
        imageSound = (ImageView) view.findViewById(R.id.image_sound);
        imageAirQuality = (ImageView) view.findViewById(R.id.image_air_quality);
        imageHumidity = (ImageView) view.findViewById(R.id.image_humidity);
        imageTemperature = (ImageView) view.findViewById(R.id.image_temperature);
        imageBarometricPressure = (ImageView) view.findViewById(R.id.image_barometric_pressure);
        imageAnalogUnkown = (ImageView) view.findViewById(R.id.image_analog_unknown);
        imageNoSensor = (ImageView) view.findViewById(R.id.image_no_sensor);

        imageLight.setOnClickListener(this);
        imageSoilMoisture.setOnClickListener(this);
        imageDistance.setOnClickListener(this);
        imageSound.setOnClickListener(this);
        imageAirQuality.setOnClickListener(this);
        imageHumidity.setOnClickListener(this);
        imageTemperature.setOnClickListener(this);
        imageBarometricPressure.setOnClickListener(this);
        imageAnalogUnkown.setOnClickListener(this);
        imageNoSensor.setOnClickListener(this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onClick(View view) {
        Sensor sensor = new NoSensor(portNumber);
        switch (view.getId()) {
            case R.id.image_light:
                Log.d(Constants.LOG_TAG, "onClickLightSensor");
                sensor = new LightSensor(portNumber);
                break;
            case R.id.image_soil_moisture:
                Log.d(Constants.LOG_TAG, "onClickSoilMoistureSensor");
                sensor = new SoilMoistureSensor(portNumber);
                break;
            case R.id.image_distance:
                Log.d(Constants.LOG_TAG, "onClickDistanceSensor");
                sensor = new DistanceSensor(portNumber);
                break;
            case R.id.image_sound:
                Log.d(Constants.LOG_TAG, "onClickSoundSensor");
                sensor = new SoundSensor(portNumber);
                break;
            case R.id.image_air_quality:
                Log.d(Constants.LOG_TAG, "onClickWindSpeedSensor");
                sensor = new AirQualitySensor(portNumber);
                break;
            case R.id.image_humidity:
                Log.d(Constants.LOG_TAG, "onClickHumiditySensor");
                sensor = new HumiditySensor(portNumber);
                break;
            case R.id.image_temperature:
                Log.d(Constants.LOG_TAG, "onClickTemperatureSensor");
                sensor = new TemperatureSensor(portNumber);
                break;
            case R.id.image_barometric_pressure:
                Log.d(Constants.LOG_TAG, "onClickBarometricPressureSensor");
                sensor = new BarometricPressureSensor(portNumber);
                break;
            case R.id.image_analog_unknown:
                Log.d(Constants.LOG_TAG, "onClickAnalogUnknownSensor");
                sensor = new AnalogOrUnknownSensor(portNumber);
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
