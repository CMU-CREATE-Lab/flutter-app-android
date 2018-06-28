package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
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

    public class State implements Serializable {
        private BaseResizableDialogWizard currentDialog;
        public Relationship relationshipType = NoRelationship.getInstance();
        public int selectedSensorPort=0,
                outputMin=0,
                outputMax=100;
    }
    public static final String STATE_KEY = "servo_wizard_state";


    private BaseResizableDialogWizard goTo(int page) {
        // TODO @tasota consider all cases and default dialog (not null)
        switch(page) {
            case 1:
                return ChooseRelationshipOutputDialogWizard.newInstance(this, currentState);
            case 2:
                return ChooseSensorOutputDialogWizard.newInstance(this, currentState);
            case 3:
                return ChoosePositionServoDialogWizard.newInstance(this, currentState, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN);
            case 4:
                return ChoosePositionServoDialogWizard.newInstance(this, currentState, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX);
            default:
                break;
        }
        return null;
    }


    public ServoWizard(RobotActivity activity, Servo servo) {
        this.activity = activity;
        this.servo = Servo.newInstance(servo);
        this.currentState = new State();
    }

    public void start() {
        currentState.currentDialog = goTo(1);
        currentState.currentDialog.show(activity.getSupportFragmentManager(), "tag");
    }

    public void changeDialog(Bundle options) {
        int page = options.getInt("page");
        this.currentState = (State) options.getSerializable(STATE_KEY);
        BaseResizableDialogWizard nextDialog = goTo(page);
        if (nextDialog == null) {
            // finish

            // TODO @tasota avoid crash when something wasn't set
            if (currentState.relationshipType.getClass() == NoRelationship.class) {
                currentState.relationshipType = Proportional.getInstance();
            }
            if (currentState.selectedSensorPort < 1 || currentState.selectedSensorPort > 3) {
                currentState.selectedSensorPort = 1;
            }

            SettingsProportional newSettings = SettingsProportional.newInstance(servo.getSettings());
            newSettings.setSensorPortNumber(currentState.selectedSensorPort);
            newSettings.setOutputMin(currentState.outputMin);
            newSettings.setOutputMax(currentState.outputMax);
            servo.setSettings( Settings.newInstance(newSettings, currentState.relationshipType) );
            servo.setIsLinked(true, servo);

            ServoDialog dialog = ServoDialog.newInstance(servo, activity, false);
            dialog.show(activity.getSupportFragmentManager(), "tag");

            currentState.currentDialog.dismiss();
        } else {
            nextDialog.show(activity.getSupportFragmentManager(), "tag");
            currentState.currentDialog.dismiss();
            currentState.currentDialog = nextDialog;
        }
    }

}
