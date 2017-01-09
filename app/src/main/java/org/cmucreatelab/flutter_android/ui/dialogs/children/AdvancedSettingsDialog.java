package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

/**
 * Created by Steve on 11/17/2016.
 */
// TODO - fix the sensor type text, just hacked it in for now
public class AdvancedSettingsDialog extends BaseResizableDialog implements DialogInterface.OnClickListener {


    private static final String ADVANCED_KEY = "advanced_key";
    private static final String OUTPUT_KEY = "output_key";


    private DialogAdvancedSettingsListener dialogAdvancedSettingsListener;
    private Output output;
    private SeekBar seekBarMaxInput;
    private SeekBar seekBarMinInput;
    private SeekBar seekBarZeroPoint;
    private TextView textMaxInput;
    private TextView textMinInput;
    private TextView textZeroPoint;

    private int maxInput;
    private int minInput;
    private int zeroPoint;
    private String sensorText;


    private SeekBar.OnSeekBarChangeListener seekBarMaxInputListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textMaxInput.setText(getString(R.string.max_input) + " " + sensorText + ": " + i + "%");
            maxInput = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private SeekBar.OnSeekBarChangeListener seekBarMinInputListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textMinInput.setText(getString(R.string.min_input) + " " + sensorText + ": " + i + "%");
            minInput = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private SeekBar.OnSeekBarChangeListener seekBarZeroPointListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onZeroPointChanged");
            textZeroPoint.setText(getString(R.string.zero_point) + " " + i + "%");
            zeroPoint = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    public static AdvancedSettingsDialog newInstance(Serializable serializable, Serializable output) {
        AdvancedSettingsDialog advancedSettingsDialog = new AdvancedSettingsDialog();

        Bundle args = new Bundle();
        args.putSerializable(ADVANCED_KEY, serializable);
        args.putSerializable(OUTPUT_KEY, output);
        advancedSettingsDialog.setArguments(args);

        return advancedSettingsDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        super.onCreateDialog(savedInstances);
        dialogAdvancedSettingsListener = (DialogAdvancedSettingsListener) getArguments().getSerializable(ADVANCED_KEY);
        output = (Output) getArguments().getSerializable(OUTPUT_KEY);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_advanced_settings, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setPositiveButton(R.string.save, this);
        builder.setTitle(getString(R.string.advanced_settings)).setView(view);

        seekBarMaxInput = (SeekBar) view.findViewById(R.id.seekbar_max_input);
        seekBarMinInput = (SeekBar) view.findViewById(R.id.seekbar_min_input);
        seekBarZeroPoint = (SeekBar) view.findViewById(R.id.seekbar_zero_point);
        textMaxInput = (TextView) view.findViewById(R.id.text_max_input);
        textMinInput = (TextView) view.findViewById(R.id.text_min_input);
        textZeroPoint = (TextView) view.findViewById(R.id.text_zero_point);

        maxInput = 100;
        minInput = 0;
        zeroPoint = 0;

        seekBarMaxInput.setOnSeekBarChangeListener(seekBarMaxInputListener);
        seekBarMinInput.setOnSeekBarChangeListener(seekBarMinInputListener);
        seekBarZeroPoint.setOnSeekBarChangeListener(seekBarZeroPointListener);
        seekBarMaxInput.setProgress(maxInput);
        seekBarMinInput.setProgress(minInput);
        seekBarZeroPoint.setProgress(zeroPoint);

        /*if (output.getOutputType() == Output.Type.SERVO)
            sensorText = ((Servo) output).getServoSettings().getSensor().getSensorType().toString();
        if (output.getOutputType() == Output.Type.LED)
            sensorText = ((Led) output).getRedSettings().getSensor().getSensorType().toString();
        if (output.getOutputType() == Output.Type.SPEAKER)
            sensorText = ((Speaker) output).getVolumeSettings().getSensor().getSensorType().toString();*/

        sensorText = getString(output.getSettings().getSensor().getSensorTypeId());

        textMaxInput.setText(getString(R.string.max_input) + " " + sensorText + ": " + maxInput + "%");
        textMinInput.setText(getString(R.string.min_input) + " " + sensorText + ": " + minInput + "%");
        textZeroPoint.setText(getString(R.string.zero_point) + " " + zeroPoint + "%");

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClick");
        AdvancedSettings result = new AdvancedSettings();
        result.setInputMax(maxInput);
        result.setInputMin(minInput);
        result.setZeroValue(zeroPoint);
        dialogAdvancedSettingsListener.onAdvancedSettingsSet(result);
    }


    // interface for an activity to listen for a choice
    public interface DialogAdvancedSettingsListener {
        public void onAdvancedSettingsSet(AdvancedSettings advancedSettings);
    }

}
