package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.led;

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
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseRelationshipServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseSensorServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ServoWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/28/18.
 */

public class ChooseColorLedDialogWizard extends BaseResizableDialogWizard {

    LedWizard.LedWizardState wizardState;

    private int selectedValue = 0;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

    public static final String DIALOG_TYPE = "dialog_type";


    public enum OUTPUT_TYPE {
        MIN, MAX
    }

    @Override
    public void onResume() {
        super.onResume();
        //getDialog().getWindow().setLayout(convertDpToPx(390), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void updateWizardState() {
        wizardState = (LedWizard.LedWizardState) (wizard.getCurrentState());
    }


    public static ChooseColorLedDialogWizard newInstance(OutputWizard wizard, OUTPUT_TYPE type) {
        Bundle args = new Bundle();
        ChooseColorLedDialogWizard dialogWizard = new ChooseColorLedDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(DIALOG_TYPE, type);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    private void updateViewWithOptions() {
        //start off at 0 for constant relationships
        if (wizardState.relationshipType == Constant.getInstance()) {
            //wizardState.outputMax = 0;
        } //else if (wizardState.outputMax == 0) {
            //wizardState.outputMax = 180;
       // }
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

        updateViewWithOptions();
        updateTextViews(view);

        return builder.create();
    }


    private void updateTextViews(View view) {
        // views
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(((Servo) wizard.getOutput()).getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
        ((TextView) view.findViewById(R.id.text_set_position)).setText(getPositionPrompt());
    }


    private String getPositionPrompt() {
        if (wizardState.relationshipType == Constant.getInstance()) {
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
            //wizardState.outputMin = selectedValue;
            wizard.changeDialog(ChooseSensorServoDialogWizard.newInstance(wizard));
        } else {
            //wizardState.outputMax = selectedValue;
            if (wizardState.relationshipType == Constant.getInstance()) {
                wizard.changeDialog(ChooseRelationshipServoDialogWizard.newInstance(wizard));
            } else {
                wizard.changeDialog(ChooseColorLedDialogWizard.newInstance(wizard, ChooseColorLedDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }


    @OnClick(R.id.button_next)
    public void onClickNext() {
        if (this.outputType == OUTPUT_TYPE.MIN) {
            //wizardState.outputMin = selectedValue;
            wizard.changeDialog(ChooseColorLedDialogWizard.newInstance(wizard, ChooseColorLedDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            //wizardState.outputMax = selectedValue;
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
