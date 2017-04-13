package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.FlutterOutput;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 11/17/2016.
 */
public class AdvancedSettingsDialog extends BaseResizableDialog {

    private static final String ADVANCED_KEY = "advanced_key";
    private static final String OUTPUT_KEY = "output_key";

    private DialogAdvancedSettingsListener dialogAdvancedSettingsListener;
    private FlutterOutput flutterOutput;
    private SeekBar seekBarMaxInput;
    private SeekBar seekBarMinInput;
    private SeekBar seekBarSpeed;
    private TextView textMaxInput;
    private TextView textMinInput;
    private TextView textSpeed;
    private int maxInput;
    private int minInput;
    private int speed;
    private String sensorText;
    private AdvancedSettings advancedSettings;


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


    private SeekBar.OnSeekBarChangeListener seekBarSpeedListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onSpeedChanged");
            textSpeed.setText(getString(R.string.speed) + " " + i + " / 15");
            speed = i;
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
        flutterOutput = (FlutterOutput) getArguments().getSerializable(OUTPUT_KEY);

        // ASSERT: settings are the same across all outputs, and there is always at least 1 output
        sensorText = getString(flutterOutput.getOutputs()[0].getSettings().getSensor().getSensorTypeId());
        Settings settings = flutterOutput.getOutputs()[0].getSettings();
        if (settings.hasAdvancedSettings()) {
            if (settings.getClass() == SettingsProportional.class) {
                this.advancedSettings = AdvancedSettings.newInstance(((SettingsProportional) settings).getAdvancedSettings());
            } else if (settings.getClass() == SettingsAmplitude.class) {
                this.advancedSettings = AdvancedSettings.newInstance(((SettingsAmplitude) settings).getAdvancedSettings());
            } else {
                Log.e(Constants.LOG_TAG,"AdvancedSettingsDialog.onCreateDialog: unimplemented relationship/settings");
            }
        }

        Log.i(Constants.LOG_TAG,"created AdvancedSettingsDialog for FlutterOutput=" + flutterOutput.getClass().getName());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_advanced_settings, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setTitle(getString(R.string.advanced_settings)).setView(view);
        ButterKnife.bind(this, view);

        seekBarMaxInput = (SeekBar) view.findViewById(R.id.seekbar_max_input);
        seekBarMinInput = (SeekBar) view.findViewById(R.id.seekbar_min_input);
        seekBarSpeed = (SeekBar) view.findViewById(R.id.seekbar_speed);
        textMaxInput = (TextView) view.findViewById(R.id.text_max_input);
        textMinInput = (TextView) view.findViewById(R.id.text_min_input);
        textSpeed = (TextView) view.findViewById(R.id.text_speed);

        // populate defaults
        maxInput = advancedSettings.getInputMax();
        minInput = advancedSettings.getInputMin();
        speed = advancedSettings.getSpeed();

        seekBarMaxInput.setOnSeekBarChangeListener(seekBarMaxInputListener);
        seekBarMinInput.setOnSeekBarChangeListener(seekBarMinInputListener);
        seekBarSpeed.setOnSeekBarChangeListener(seekBarSpeedListener);
        seekBarMaxInput.setProgress(maxInput);
        seekBarMinInput.setProgress(minInput);
        seekBarSpeed.setProgress(speed);

        textMaxInput.setText(getString(R.string.max_input) + " " + sensorText + ": " + maxInput + "%");
        textMinInput.setText(getString(R.string.min_input) + " " + sensorText + ": " + minInput + "%");
        textSpeed.setText(getString(R.string.speed) + " " + speed + " / 15");

        return builder.create();
    }


    @OnClick(R.id.button_save_link)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "AdvancedSettingsDialog.onClickSaveSettings");
        advancedSettings.setInputMax(maxInput);
        advancedSettings.setInputMin(minInput);
        advancedSettings.setSpeed(speed);
        dialogAdvancedSettingsListener.onAdvancedSettingsSet(advancedSettings);
        dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogAdvancedSettingsListener {
        public void onAdvancedSettingsSet(AdvancedSettings advancedSettings);
    }

}
