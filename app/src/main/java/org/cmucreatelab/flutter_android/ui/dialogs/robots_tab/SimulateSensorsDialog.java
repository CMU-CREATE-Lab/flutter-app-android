package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

/**
 * Created by Steve on 7/30/2017.
 */

public class SimulateSensorsDialog extends BaseResizableDialog {

    private static final String SENSORS_KEY = "sensors_key";
    private static final String SIMULATE_SENSORS_DISMISSED_KEY = "simulate_sensors_dismissed_key";

    private GlobalHandler globalHandler;
    private Sensor[] sensors;
    private SimulateSensorsDismissed simulateSensorsDismissed;
    private SeekBar seekBarSensor1, seekBarSensor2, seekBarSensor3;
    private TextView textViewSensor1Value, textViewSensor2Value, textViewSensor3Value;
    private int seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value;


    // Seekbar change listeners


    private SeekBar.OnSeekBarChangeListener seekBarSensor1Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewSensor1Value.setText(String.valueOf(i));
            seekBarSensor1Value = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            globalHandler.sessionHandler.getSession().getFlutter().setSensorValues(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value);
            GlobalHandler.getInstance(getActivity()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSimulateData(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value));
        }
    };


    private SeekBar.OnSeekBarChangeListener seekBarSensor2Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewSensor2Value.setText(String.valueOf(i));
            seekBarSensor2Value = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            globalHandler.sessionHandler.getSession().getFlutter().setSensorValues(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value);
            GlobalHandler.getInstance(getActivity()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSimulateData(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value));
        }
    };


    private SeekBar.OnSeekBarChangeListener seekBarSensor3Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewSensor3Value.setText(String.valueOf(i));
            seekBarSensor3Value = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            globalHandler.sessionHandler.getSession().getFlutter().setSensorValues(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value);
            GlobalHandler.getInstance(getActivity()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSimulateData(seekBarSensor1Value, seekBarSensor2Value, seekBarSensor3Value));
        }
    };


    public static SimulateSensorsDialog newInstance(Sensor[] sensors, Serializable simulateSensorsDismissed) {
        SimulateSensorsDialog simulateSensorsDialog = new SimulateSensorsDialog();
        Bundle args = new Bundle();
        args.putSerializable(SENSORS_KEY, sensors);
        args.putSerializable(SIMULATE_SENSORS_DISMISSED_KEY, simulateSensorsDismissed);
        simulateSensorsDialog.setArguments(args);
        return simulateSensorsDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);
        globalHandler = GlobalHandler.getInstance(getActivity());
        globalHandler.sessionHandler.getSession().setSimulatingData(true);
        globalHandler.sessionHandler.getSession().getFlutter().setSensorValues(0,0,0);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_simulate_sensors, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        simulateSensorsDismissed = (SimulateSensorsDismissed) getArguments().getSerializable(SIMULATE_SENSORS_DISMISSED_KEY);
        sensors = (Sensor[]) getArguments().getSerializable(SENSORS_KEY);
        ((ImageView) view.findViewById(R.id.image_sensor_1)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensors[0].getGreenImageId()));
        ((TextView) view.findViewById(R.id.text_sensor_1_type)).setText(getString(sensors[0].getTypeTextId()));
        textViewSensor1Value = (TextView) view.findViewById(R.id.text_sensor_1_value);
        seekBarSensor1 = (SeekBar) view.findViewById(R.id.seekbar_sensor_1);

        ((ImageView) view.findViewById(R.id.image_sensor_2)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensors[1].getGreenImageId()));
        ((TextView) view.findViewById(R.id.text_sensor_2_type)).setText(getString(sensors[1].getTypeTextId()));
        textViewSensor2Value = (TextView) view.findViewById(R.id.text_sensor_2_value);
        seekBarSensor2 = (SeekBar) view.findViewById(R.id.seekbar_sensor_2);

        ((ImageView) view.findViewById(R.id.image_sensor_3)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensors[2].getGreenImageId()));
        ((TextView) view.findViewById(R.id.text_sensor_3_type)).setText(getString(sensors[2].getTypeTextId()));
        textViewSensor3Value = (TextView) view.findViewById(R.id.text_sensor_3_value);
        seekBarSensor3 = (SeekBar) view.findViewById(R.id.seekbar_sensor_3);

        textViewSensor1Value.setText(String.valueOf(seekBarSensor1.getProgress()));
        seekBarSensor1.setOnSeekBarChangeListener(seekBarSensor1Listener);
        textViewSensor2Value.setText(String.valueOf(seekBarSensor2.getProgress()));
        seekBarSensor2.setOnSeekBarChangeListener(seekBarSensor2Listener);
        textViewSensor3Value.setText(String.valueOf(seekBarSensor3.getProgress()));
        seekBarSensor3.setOnSeekBarChangeListener(seekBarSensor3Listener);

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        simulateSensorsDismissed.onSimulateSensorsDismissed();
    }

    public interface SimulateSensorsDismissed {
        void onSimulateSensorsDismissed();
    }

}
