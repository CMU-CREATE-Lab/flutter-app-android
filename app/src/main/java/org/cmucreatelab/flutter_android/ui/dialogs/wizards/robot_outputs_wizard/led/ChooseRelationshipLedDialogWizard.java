package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.led;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseRelationshipOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChoosePositionServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseSensorServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ServoWizard;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseRelationshipLedDialogWizard extends ChooseRelationshipOutputDialogWizard {

    LedWizard.LedWizardState wizardState;

    public static ChooseRelationshipLedDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseRelationshipLedDialogWizard dialogWizard = new ChooseRelationshipLedDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }

    public void updateViewWithOptions() {
        View selectedView = getViewFromRelationship(wizardState.relationshipType);

        if (selectedView != null) {
            nextButton.setClickable(true);
            nextButton.setBackgroundResource(R.drawable.round_green_button_bottom_right);
            selectedView(selectedView);
        } else {
            nextButton.setClickable(false);
            clearSelection();
        }
    }

    public void updateRelationshipType(View view) {
        wizardState.relationshipType = getRelationshipFromId(view.getId());
    }

	public void updateTitle(View view)
	{
		((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " + String.valueOf(((TriColorLed) wizard.getOutput()).getPortNumber()));
		((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
	}

    public void updateWizardState()
    {
        wizardState = (LedWizard.LedWizardState)(wizard.getCurrentState());
    }

    public void onClickNext() {
		Log.d(LOG_TAG, "onClickNext() called");

        if (getViewFromRelationship(wizardState.relationshipType) != null) {
            if (wizardState.relationshipType == Constant.getInstance()) {
                wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
            } else {
                wizard.changeDialog(ChooseSensorLedDialogWizard.newInstance(wizard));
            }
        }
    }
}
