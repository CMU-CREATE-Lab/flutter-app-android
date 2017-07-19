package org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;

import java.io.Serializable;

/**
 * Created by Steve on 4/3/2017.
 */

public class BlueSensorTypeDialog extends SensorTypeDialog {


    public static SensorTypeDialog newInstance(int portNumber, Serializable serializable) {
        SensorTypeDialog sensorTypeDialog = new BlueSensorTypeDialog();

        Bundle args = new Bundle();
        args.putInt(Constants.SerializableKeys.SENSOR_PORT_KEY, portNumber);
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        sensorTypeDialog.setArguments(args);

        return sensorTypeDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        imageLight.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_light_l_g_68));
        imageSoilMoisture.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_soil_moisture_l_g_68));
        imageDistance.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_distance_l_g_68));
        imageSound.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_sound_l_g_68));
        imageAirQuality.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_air_quality_l_g_68));
        imageHumidity.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_humidity_l_g_68));
        imageTemperature.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_temperature_l_g_68));
        imageBarometricPressure.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_barometric_l_g_68));
        imageAnalogUnknown.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_analog_l_g_68));
        imageNoSensor.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sensor_blue_no_sensor));

        return dialog;
    }
}
