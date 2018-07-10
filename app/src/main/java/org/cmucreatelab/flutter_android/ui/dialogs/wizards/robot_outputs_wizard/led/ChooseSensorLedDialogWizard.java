package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.led;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseSensorOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChoosePositionServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseRelationshipServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ServoWizard;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by Parv on 7/10/18.
 */

public class ChooseSensorLedDialogWizard extends ChooseSensorOutputDialogWizard
{
    public static ChooseSensorLedDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseSensorLedDialogWizard dialogWizard = new ChooseSensorLedDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }

	public void updateViewWithOptions() {
		LedWizard.LedWizardState wizardState = (LedWizard.LedWizardState)(wizard.getCurrentState());
		View selectedView = getViewFromSensorPort(wizardState.selectedSensorPort);

		if (selectedView != null) {
			nextButton.setClickable(true);
			nextButton.setBackgroundResource(R.drawable.round_green_button_bottom_right);
			selectedView(selectedView);
		} else {
			nextButton.setClickable(false);
			clearSelection();
		}
	}

	public void updateSelectedSensorPort(View view) {
		LedWizard.LedWizardState wizardState = (LedWizard.LedWizardState)(wizard.getCurrentState());
		wizardState.selectedSensorPort = getSensorPortFromId(view.getId());
	}

	public void updateTitle(View view)
	{
		((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " + String.valueOf(((TriColorLed) wizard.getOutput()).getPortNumber()));
		((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
	}


	public void onClickBack() {
		wizard.changeDialog(ChooseRelationshipLedDialogWizard.newInstance(wizard));
	}


    public void onClickNext() {
		Log.d(LOG_TAG, "onClickNext() called");
		LedWizard.LedWizardState wizardState = (LedWizard.LedWizardState)(wizard.getCurrentState());

        if (getViewFromSensorPort(wizardState.selectedSensorPort) != null) {
            wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN));
        }
    }
}
