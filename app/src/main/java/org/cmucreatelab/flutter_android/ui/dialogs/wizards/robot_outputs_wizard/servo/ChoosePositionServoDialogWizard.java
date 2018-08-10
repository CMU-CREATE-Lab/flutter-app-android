package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/28/18.
 *
 * ChoosePositionServoDialogWizard
 *
 * A class for choosing the servo position for the servo wizard.
 */
public class ChoosePositionServoDialogWizard extends BaseResizableDialogWizard {

    ServoWizard.ServoWizardState wizardState;

    private int selectedValue = 0;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

    private static final String DIALOG_TYPE = "dialog_type";


    public enum OUTPUT_TYPE {
        MIN, MAX
    }


    ////  pointer helper
    private ImageView pointer;
    private TextView curentPosition;
    private SeekBar seekBarMaxMin;


    private void updatePointer() {
        RotateAnimation rotateAnimation = new RotateAnimation(selectedValue - 2, selectedValue - 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        pointer.startAnimation(rotateAnimation);
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(410), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @OnClick(R.id.button_increment)
    public void onClickIncrementProgressBar() {
        seekBarMaxMin.setProgress(seekBarMaxMin.getProgress() + 1);
    }


    @OnClick(R.id.button_decrement)
    public void onClickDecrementProgressBar() {
        seekBarMaxMin.setProgress(seekBarMaxMin.getProgress() - 1);
    }


    public void updateWizardState() {
        wizardState = (ServoWizard.ServoWizardState) (wizard.getCurrentState());
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged: selectedValue=" + selectedValue);
            selectedValue = i;
            curentPosition.setText(String.valueOf(selectedValue) + (char) 0x00B0);
            updatePointer();
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
    //// END pointer helper


    public static ChoosePositionServoDialogWizard newInstance(OutputWizard wizard, OUTPUT_TYPE type) {
        Bundle args = new Bundle();
        ChoosePositionServoDialogWizard dialogWizard = new ChoosePositionServoDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(DIALOG_TYPE, type);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    private void updateViewWithOptions() {
        //start off at 0 for constant relationships
        if (wizardState.relationshipType instanceof Constant && wizardState.outputMax == 180) {
            wizardState.outputMax = 0;
        } else if (wizardState.outputMax == 0) {
            wizardState.outputMax = 180;
        }

        if (this.outputType == OUTPUT_TYPE.MIN) {
            seekBarMaxMin.setProgress(wizardState.outputMin);
        } else {
            seekBarMaxMin.setProgress(wizardState.outputMax);
        }
        updatePointer();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_position_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.outputType = (OUTPUT_TYPE) (getArguments().getSerializable(DIALOG_TYPE));

        Button nextButton = (Button) view.findViewById(R.id.button_next);
        nextButton.setBackgroundResource(R.drawable.round_green_button_bottom_right);

        updateWizardState();

        // grab info
        pointer = (ImageView) view.findViewById(R.id.image_servo_pointer);
        curentPosition = (TextView) view.findViewById(R.id.text_current_angle);
        seekBarMaxMin = (SeekBar) view.findViewById(R.id.seek_position);
        seekBarMaxMin.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarMaxMin.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        updateViewWithOptions();
        updateTextAndAudio(view);
        view.findViewById(R.id.button_set_position).setVisibility(View.GONE);

        return builder.create();
    }


    private void updateTextAndAudio(View view) {
        // views
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(((Servo) wizard.getOutput()).getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
        ((TextView) view.findViewById(R.id.text_set_position)).setText(getPositionPrompt());

        if (wizardState.relationshipType instanceof Constant)
            flutterAudioPlayer.addAudio(R.raw.audio_10);
        else
            flutterAudioPlayer.addAudio(R.raw.audio_08);

        flutterAudioPlayer.playAudio();
    }


    private String getPositionPrompt() {
        if (!(wizardState.relationshipType instanceof Constant)) {
            Sensor[] sensors = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();

            switch (outputType) {
                case MIN:
                    return "Set the " + getString(sensors[wizardState.selectedSensorPort - 1].getLowTextId()).toLowerCase() + " position";
                default:
                    return "Set the " + getString(sensors[wizardState.selectedSensorPort - 1].getHighTextId()).toLowerCase() + " position";
            }
        } else {
            return "Set the constant position";
        }
    }


    @OnClick(R.id.button_back)
    public void onClickBack() {
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizardState.outputMin = selectedValue;
            wizard.changeDialog(ChooseSensorServoDialogWizard.newInstance(wizard));
        } else {
            wizardState.outputMax = selectedValue;
            if (wizardState.relationshipType instanceof Constant) {
                wizard.changeDialog(ChooseRelationshipServoDialogWizard.newInstance(wizard));
            } else {
                wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }


    @OnClick(R.id.button_next)
    public void onClickNext() {
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizardState.outputMin = selectedValue;
            wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizardState.outputMax = selectedValue;
            wizard.finish();
        }

    }
}
