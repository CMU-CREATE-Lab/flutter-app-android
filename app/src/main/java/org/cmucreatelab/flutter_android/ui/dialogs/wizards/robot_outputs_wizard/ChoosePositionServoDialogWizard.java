package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/28/18.
 */

public class ChoosePositionServoDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    private int selectedValue = 0;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

//    public static final String SELECTED_VALUE = "selected_value";
    public static final String DIALOG_TYPE = "dialog_type";

    enum OUTPUT_TYPE {
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


    public static ChoosePositionServoDialogWizard newInstance(ServoWizard wizard, OUTPUT_TYPE type) {
        Bundle args = new Bundle();
        ChoosePositionServoDialogWizard dialogWizard = new ChoosePositionServoDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(DIALOG_TYPE, type);
        dialogWizard.setArguments(args);

        return dialogWizard;
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

        return builder.create();
    }

    @OnClick(R.id.button_back_page)
    public void onClickBack() {
        ServoWizard.State wizardState = wizard.getCurrentState();
        wizardState.interaction = ServoWizard.Interactions.CLICK_BACK;
        Bundle args = new Bundle();

        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizard.changeDialog(args, ChooseSensorOutputDialogWizard.newInstance(wizard));
        } else {
            if (wizardState.relationshipType == Constant.getInstance()) {
                wizard.changeDialog(args, ChooseRelationshipOutputDialogWizard.newInstance(wizard));
            } else {
                wizard.changeDialog(args, ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }

    @OnClick(R.id.button_next_page)
    public void onClickSave() {
        ServoWizard.State wizardState = wizard.getCurrentState();
        wizardState.interaction = ServoWizard.Interactions.CLICK_NEXT;
        Bundle args = new Bundle();
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizardState.outputMin = selectedValue;
        } else {
            wizardState.outputMax = selectedValue;
        }
        args.putSerializable(DIALOG_TYPE, outputType);
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizard.changeDialog(args, ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizard.finish();
        }

    }

    public OUTPUT_TYPE getOutputType() {
        return outputType;
    }

}
