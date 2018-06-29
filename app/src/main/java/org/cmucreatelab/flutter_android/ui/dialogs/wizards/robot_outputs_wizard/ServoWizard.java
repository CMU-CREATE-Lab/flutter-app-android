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
        public Interactions interaction;
        public Relationship relationshipType = NoRelationship.getInstance();
        public int selectedSensorPort=0,
                outputMin=0,
                outputMax=100;
    }

    enum Interactions {
        CLICK_BACK, CLICK_NEXT, CLICK_CANCEL
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
        currentState.currentDialog = ChooseRelationshipOutputDialogWizard.newInstance(this);
        currentState.currentDialog.show(activity.getSupportFragmentManager(), "tag");
    }

    public void finish() {
        generateSettings(this.currentState, this.servo);
        ServoDialog dialog = ServoDialog.newInstance(servo, activity, false);
        dialog.show(activity.getSupportFragmentManager(), "tag");
        currentState.currentDialog.dismiss();
    }

    public void changeDialog(Bundle options, BaseResizableDialogWizard nextDialog) {
        if (nextDialog == null) {
            Log.e(Constants.LOG_TAG, "found null nextDialog; ending wizard");
            currentState.currentDialog.dismiss();
            return;
        }

        nextDialog.show(activity.getSupportFragmentManager(), "tag");
        currentState.currentDialog.dismiss();
        currentState.currentDialog = nextDialog;
    }

    public State getCurrentState() {
        return currentState;
    }

}
