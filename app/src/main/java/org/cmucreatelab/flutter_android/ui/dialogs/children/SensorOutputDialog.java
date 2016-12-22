package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import java.io.Serializable;

/**
 * Created by Steve on 9/1/2016.
 *
 * SensorOutputDialog
 *
 * A Dialog that shows which sensor is to be linked with an output.
 */
public class SensorOutputDialog extends BaseResizableDialog implements View.OnClickListener  {


    private DialogSensorListener dialogSensorListener;


    public static SensorOutputDialog newInstance(Serializable serializable) {
        SensorOutputDialog sensorDialog = new SensorOutputDialog();

        Bundle args = new Bundle();
        args.putSerializable(Sensor.SENSOR_KEY, serializable);
        sensorDialog.setArguments(args);

        return sensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        super.onCreateDialog(savedInstances);
        dialogSensorListener = (DialogSensorListener) getArguments().getSerializable(Sensor.SENSOR_KEY);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensors, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setTitle(getString(R.string.choose_sensor)).setView(view);

        Sensor sensors[] = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getFlutter().getSensors();

        // bind click listeners
        ImageView sensor1 = (ImageView) view.findViewById(R.id.image_sensor_1);
        ImageView sensor2 = (ImageView) view.findViewById(R.id.image_sensor_2);
        ImageView sensor3 = (ImageView) view.findViewById(R.id.image_sensor_3);
        TextView textSensor1 = (TextView) view.findViewById(R.id.text_sensor_1);
        TextView textSensor2 = (TextView) view.findViewById(R.id.text_sensor_2);
        TextView textSensor3 = (TextView) view.findViewById(R.id.text_sensor_3);

        sensor1.setOnClickListener(this);
        sensor2.setOnClickListener(this);
        sensor3.setOnClickListener(this);

        sensor1.setImageResource(sensors[0].getGreenImageId());
        sensor2.setImageResource(sensors[1].getGreenImageId());
        sensor3.setImageResource(sensors[2].getGreenImageId());
        textSensor1.setText(sensors[0].getSensorTypeId());
        textSensor2.setText(sensors[1].getSensorTypeId());
        textSensor3.setText(sensors[2].getSensorTypeId());

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(view.getContext());
        Sensor sensor = new NoSensor(0);
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
