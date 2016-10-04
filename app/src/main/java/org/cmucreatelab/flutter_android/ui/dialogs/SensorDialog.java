package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.Serializable;

/**
 * Created by Steve on 9/1/2016.
 */
public class SensorDialog extends DialogFragment implements View.OnClickListener  {


    private DialogSensorListener dialogSensorListener;


    public static SensorDialog newInstance(Serializable serializable) {
        SensorDialog sensorDialog = new SensorDialog();

        Bundle args = new Bundle();
        args.putSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY, serializable);
        sensorDialog.setArguments(args);

        return sensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        super.onCreateDialog(savedInstances);
        dialogSensorListener = (DialogSensorListener) getArguments().getSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensors, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.choose_sensor)).setView(view);

        // bind click listeners
        view.findViewById(R.id.image_sensor_1).setOnClickListener(this);
        view.findViewById(R.id.image_sensor_2).setOnClickListener(this);
        view.findViewById(R.id.image_sensor_3).setOnClickListener(this);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler = GlobalHandler.newInstance(view.getContext());
        Sensor sensor = new NoSensor();
        switch (view.getId()) {
            case R.id.image_sensor_1:
                sensor = globalHandler.sessionHandler.getFlutter().getSensors()[0];
                break;
            case R.id.image_sensor_2:
                sensor = globalHandler.sessionHandler.getFlutter().getSensors()[1];
                break;
            case R.id.image_sensor_3:
                sensor = globalHandler.sessionHandler.getFlutter().getSensors()[2];
                break;

        }
        dialogSensorListener.onSensorChosen(sensor);
        this.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogSensorListener {
        public void onSensorChosen(Sensor sensor);
    }

}
