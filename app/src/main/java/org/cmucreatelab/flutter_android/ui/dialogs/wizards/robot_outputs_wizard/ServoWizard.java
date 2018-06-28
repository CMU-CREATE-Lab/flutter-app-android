package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
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
    private BaseResizableDialogWizard currentDialog;
//    enum ACT {
//        ONE,TWO,THREE
//    }

    public enum ACTIONS {

    }

    private BaseResizableDialogWizard goTo(int page) {
        // TODO @tasota consider all cases and default dialog (not null)
        switch(page) {
            case 1:
                return ChooseRelationshipOutputDialogWizard.newInstance(this);
            case 2:
                return ChooseSensorOutputDialogWizard.newInstance(this);
            case 3:
                return ChoosePositionServoDialogWizard.newInstance(this, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MIN);
            case 4:
                return ChoosePositionServoDialogWizard.newInstance(this, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX);
            default:
                break;
        }
        return null;
    }


    public ServoWizard(RobotActivity activity, Servo servo) {
        this.activity = activity;
        this.servo = Servo.newInstance(servo);
    }

    public void start() {
        this.currentDialog = goTo(1);
        currentDialog.show(activity.getSupportFragmentManager(), "tag");
    }

    public void changeDialog(Bundle options) {
        int page = options.getInt("page");
        BaseResizableDialogWizard nextDialog = goTo(page);
        if (nextDialog == null) {
            // finish

            SettingsProportional newSettings = SettingsProportional.newInstance(servo.getSettings());
            newSettings.setSensorPortNumber(1);
            newSettings.setOutputMin(11);
            newSettings.setOutputMax(22);
            servo.setSettings(newSettings);
            servo.setIsLinked(true, servo);

            ServoDialog dialog = ServoDialog.newInstance(servo, activity, false);
            dialog.show(activity.getSupportFragmentManager(), "tag");

            currentDialog.dismiss();
        } else {
            nextDialog.show(activity.getSupportFragmentManager(), "tag");
            currentDialog.dismiss();
            this.currentDialog = nextDialog;
        }
    }

}
