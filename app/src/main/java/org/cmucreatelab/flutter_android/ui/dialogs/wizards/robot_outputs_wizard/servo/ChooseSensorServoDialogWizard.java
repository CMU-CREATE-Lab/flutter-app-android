package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseSensorOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by Parv on 7/10/18.
 */

public class ChooseSensorServoDialogWizard extends ChooseSensorOutputDialogWizard
{
	ServoWizard.ServoWizardState wizardState;

    public static ChooseSensorServoDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseSensorServoDialogWizard dialogWizard = new ChooseSensorServoDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }

	public void updateViewWithOptions() {
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
		wizardState.selectedSensorPort = getSensorPortFromId(view.getId());
	}

	public void updateTextAndAudio(View view)
	{
		((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(((Servo) wizard.getOutput()).getPortNumber()));
		((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
		((TextView) view.findViewById(R.id.text_sensor_prompt)).setText(getString(R.string.servo_sensor_prompt));

		audioPlayer.addAudio(R.raw.a_07);
		audioPlayer.playAudio();
	}


	public void onClickBack() {
		wizard.changeDialog(ChooseRelationshipServoDialogWizard.newInstance(wizard));
	}

	public void updateWizardState()
	{
		wizardState = (ServoWizard.ServoWizardState)(wizard.getCurrentState());
	}

    public void onClickNext() {
		Log.d(LOG_TAG, "onClickNext() called");

        if (getViewFromSensorPort(wizardState.selectedSensorPort) != null) {
            wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN));
        }
    }
}
