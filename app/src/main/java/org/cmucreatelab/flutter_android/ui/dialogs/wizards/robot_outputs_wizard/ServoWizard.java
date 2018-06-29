package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizardOld;

import java.io.Serializable;

/**
 * Created by mike on 6/27/18.
 */

public class ServoWizard implements Serializable {

    RobotActivity activity;
    Servo servo;

    private State currentState;
    private boolean isFinished = false;

    public class State implements Serializable {
        private BaseResizableDialogWizard currentDialog;
        public Interactions interaction;
        public Relationship relationshipType = NoRelationship.getInstance();
        public int selectedSensorPort=0,
                outputMin=0,
                outputMax=100;
    }
//    public static final String STATE_KEY = "servo_wizard_state";

    enum Interactions {
        CLICK_BACK, CLICK_NEXT, CLICK_CANCEL
    }

    private BaseResizableDialogWizard findNext() {
        // TODO @tasota consider all cases and default dialog (not null)
        BaseResizableDialogWizard result = null;
        if (currentState.currentDialog == null) {
            Log.e(Constants.LOG_TAG, "found null in findNext");
        } else if (currentState.currentDialog.getClass() == ChooseRelationshipOutputDialogWizard.class) {
            if (currentState.interaction == Interactions.CLICK_NEXT) {
                result = ChooseSensorOutputDialogWizard.newInstance(this);
            } else if (currentState.interaction == Interactions.CLICK_BACK) {
                // cancel
            }
        } else if (currentState.currentDialog.getClass() == ChooseSensorOutputDialogWizard.class) {
            if (currentState.interaction == Interactions.CLICK_NEXT) {
                result = ChoosePositionServoDialogWizard.newInstance(this, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN);
            } else if (currentState.interaction == Interactions.CLICK_BACK) {
                result = ChooseRelationshipOutputDialogWizard.newInstance(this);
            }
        }else if (currentState.currentDialog.getClass() == ChoosePositionServoDialogWizard.class) {
            ChoosePositionServoDialogWizard positionDialog = (ChoosePositionServoDialogWizard)currentState.currentDialog;

            if (positionDialog.getOutputType() == ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN) {
                if (currentState.interaction == Interactions.CLICK_NEXT) {
                    result = ChoosePositionServoDialogWizard.newInstance(this, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX);
                } else if (currentState.interaction == Interactions.CLICK_BACK) {
                    result = ChooseSensorOutputDialogWizard.newInstance(this);
                }
            } else {
                if (currentState.interaction == Interactions.CLICK_NEXT) {
                    // finish
                    isFinished = true;
                } else if (currentState.interaction == Interactions.CLICK_BACK) {
                    result = ChoosePositionServoDialogWizard.newInstance(this, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN);
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "did not match class in findNext: "+currentState.currentDialog.getClass());
        }

        return result;
    }


    private static void generateSettings(State currentState, Output output) {
        // TODO @tasota avoid crash when something wasn't set
        if (currentState.relationshipType.getClass() == NoRelationship.class) {
            currentState.relationshipType = Proportional.getInstance();
        }
        if (currentState.selectedSensorPort < 1 || currentState.selectedSensorPort > 3) {
            currentState.selectedSensorPort = 1;
        }

        SettingsProportional newSettings = SettingsProportional.newInstance(output.getSettings());
        newSettings.setSensorPortNumber(currentState.selectedSensorPort);
        newSettings.setOutputMin(currentState.outputMin);
        newSettings.setOutputMax(currentState.outputMax);
        output.setSettings( Settings.newInstance(newSettings, currentState.relationshipType) );
        output.setIsLinked(true, output);
    }


    public ServoWizard(RobotActivity activity, Servo servo) {
        this.activity = activity;
        this.servo = Servo.newInstance(servo);
        this.currentState = new State();
    }

    public void start() {
//        currentState.currentDialog = goTo(1);
        currentState.currentDialog = ChooseRelationshipOutputDialogWizard.newInstance(this);
        currentState.currentDialog.show(activity.getSupportFragmentManager(), "tag");
    }

    public void changeDialog(Bundle options) {
//        this.currentState = (State) options.getSerializable(STATE_KEY);
//        BaseResizableDialogWizard nextDialog = goTo(page);
        BaseResizableDialogWizard nextDialog = findNext();
        if (nextDialog == null) {
            if (isFinished) {
                // finish
                generateSettings(this.currentState, this.servo);

                ServoDialog dialog = ServoDialog.newInstance(servo, activity, false);
                dialog.show(activity.getSupportFragmentManager(), "tag");
            } else {
                Log.e(Constants.LOG_TAG, "found null nextDialog but isFinished==false; ending wizard");
            }
            currentState.currentDialog.dismiss();
        } else {
            nextDialog.show(activity.getSupportFragmentManager(), "tag");
            currentState.currentDialog.dismiss();
            currentState.currentDialog = nextDialog;
        }
    }

    public State getCurrentState() {
        return currentState;
    }

}
