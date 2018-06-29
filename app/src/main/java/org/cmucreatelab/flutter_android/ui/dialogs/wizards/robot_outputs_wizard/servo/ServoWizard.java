package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

/**
 * Created by mike on 6/27/18.
 */

public class ServoWizard extends OutputWizard<Servo> {


    public ServoWizard(RobotActivity activity, Servo servo) {
        super(activity, Servo.newInstance(servo));
    }


    public BaseOutputDialog generateOutputDialog(Servo output, RobotActivity activity) {
        return ServoDialog.newInstance(output, activity);
    }

}
