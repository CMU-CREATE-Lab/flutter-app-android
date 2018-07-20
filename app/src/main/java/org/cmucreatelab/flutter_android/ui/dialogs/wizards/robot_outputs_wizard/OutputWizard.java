package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.FlutterOutput;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.Wizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseRelationshipServoDialogWizard;

import java.io.Serializable;

/**
 * Created by mike on 6/29/18.
 */

public abstract class OutputWizard<T extends FlutterOutput> extends Wizard {

    private RobotActivity activity;
    private T output;

    public abstract class State implements Serializable {
        private BaseResizableDialog currentDialog;
    }


    public abstract void createState();


    public OutputWizard(RobotActivity activity, T output) {
        this.activity = activity;
        this.output = output;
        createState();
    }


    public abstract State getCurrentState();

    public T getOutput()
	{
		return output;
	}


    @Override
    public abstract void start();


	public void startDialog(BaseResizableDialog startDialog) {
		getCurrentState().currentDialog = startDialog;
		getCurrentState().currentDialog.show(activity.getSupportFragmentManager(), "tag");
	}


    @Override
    public void finish() {
        generateSettings(this.output);
        BaseOutputDialog dialog = generateOutputDialog(output, activity);
        dialog.show(activity.getSupportFragmentManager(), "tag");
        getCurrentState().currentDialog.dismiss();
    }


    @Override
    public void changeDialog(BaseResizableDialog nextDialog) {
        if (nextDialog == null) {
            Log.e(Constants.LOG_TAG, "found null nextDialog; ending wizard");
            getCurrentState().currentDialog.dismiss();
            return;
        }

        nextDialog.show(activity.getSupportFragmentManager(), "tag");
        getCurrentState().currentDialog.dismiss();
        getCurrentState().currentDialog = nextDialog;
    }


    public abstract void generateSettings(T output);


    public abstract BaseOutputDialog generateOutputDialog(T output, RobotActivity activity);

}
