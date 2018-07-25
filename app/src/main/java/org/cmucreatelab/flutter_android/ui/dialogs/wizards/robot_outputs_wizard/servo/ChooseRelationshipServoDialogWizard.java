package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Cumulative;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.relationships.Switch;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseRelationshipOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseRelationshipServoDialogWizard extends ChooseRelationshipOutputDialogWizard {

	ServoWizard.ServoWizardState wizardState;

    public static ChooseRelationshipServoDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseRelationshipServoDialogWizard dialogWizard = new ChooseRelationshipServoDialogWizard();
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

	public void updateText(View view)
	{
		((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(((Servo) wizard.getOutput()).getPortNumber()));
		((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
        ((TextView) view.findViewById(R.id.text_relationship_prompt)).setText(getString(R.string.servo_relationship_prompt));
	}

	public void updateWizardState()
	{
		wizardState = (ServoWizard.ServoWizardState)(wizard.getCurrentState());
	}

    public void onClickNext() {
		Log.d(LOG_TAG, "onClickNext() called");

        if (getViewFromRelationship(wizardState.relationshipType) != null) {
            if (wizardState.relationshipType == Constant.getInstance()) {
                wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
            } else {
                wizard.changeDialog(ChooseSensorServoDialogWizard.newInstance(wizard));
            }
        }
    }
}
