package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import android.app.Dialog;
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
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseRelationshipOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseSensorOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/28/18.
 */

public class ChoosePositionServoDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    private int selectedValue = 0;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

    public static final String DIALOG_TYPE = "dialog_type";

    public enum OUTPUT_TYPE {
        MIN, MAX
    }

    ////  pointer helper
    private ImageView pointer;
    private TextView curentPosition;
    private SeekBar seekBarMaxMin;


    private void updatePointer() {
        RotateAnimation rotateAnimation = new RotateAnimation(selectedValue -1, selectedValue, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        pointer.startAnimation(rotateAnimation);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(390), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged: selectedValue="+selectedValue);
            selectedValue = i;
            curentPosition.setText(String.valueOf(selectedValue) + (char) 0x00B0);
            updatePointer();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
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
        ServoWizard.ServoWizardState wizardState = (ServoWizard.ServoWizardState)(wizard.getCurrentState());
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
        final View view = inflater.inflate(R.layout.dialog_wet_position_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.dialogView = view;
        this.outputType = (OUTPUT_TYPE)(getArguments().getSerializable(DIALOG_TYPE));

        Button nextButton = (Button) view.findViewById(R.id.button_next);
        nextButton.setBackgroundResource(R.drawable.round_green_button_bottom_right);

        // views
        // TODO @tasota get real port #
        //String.valueOf(currentServo.getPortNumber())
        String portNumber = "1";
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " +  portNumber);
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);

        // grab info
        pointer = (ImageView) view.findViewById(R.id.image_servo_pointer);
        curentPosition = (TextView) view.findViewById(R.id.text_current_angle);
        seekBarMaxMin = (SeekBar) view.findViewById(R.id.seek_position);
        seekBarMaxMin.setOnSeekBarChangeListener(seekBarChangeListener);

        updateViewWithOptions();

        return builder.create();
    }


    @OnClick(R.id.button_back)
    public void onClickBack() {
        ServoWizard.ServoWizardState wizardState = (ServoWizard.ServoWizardState)(wizard.getCurrentState());

        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizard.changeDialog(ChooseSensorOutputDialogWizard.newInstance(wizard));
        } else {
            if (wizardState.relationshipType == Constant.getInstance()) {
                wizard.changeDialog(ChooseRelationshipOutputDialogWizard.newInstance(wizard));
            } else {
                wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }


    @OnClick(R.id.button_next)
    public void onClickSave() {
        ServoWizard.ServoWizardState wizardState = (ServoWizard.ServoWizardState)(wizard.getCurrentState());
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizardState.outputMin = selectedValue;
        } else {
            wizardState.outputMax = selectedValue;
        }
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizard.finish();
        }

    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.i(Constants.LOG_TAG, "onClickAdvancedSettings");
        // TODO finish wizard, display summary/advanced dialog
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }

}
