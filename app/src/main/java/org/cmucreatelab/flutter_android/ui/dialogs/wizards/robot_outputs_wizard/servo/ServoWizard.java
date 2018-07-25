package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

/**
 * Created by mike on 6/27/18.
 */

public class ServoWizard extends OutputWizard<Servo> {

    private ServoWizardState currentState;

    public class ServoWizardState extends OutputWizard<Servo>.State {
        public Relationship relationshipType = NoRelationship.getInstance();
        public int selectedSensorPort=0,
                outputMin=0,
                outputMax=180;
    }


    @Override
    public void createState() {
        currentState = new ServoWizardState();
    }


    @Override
    public State getCurrentState() {
        return currentState;
    }


    public ServoWizard(RobotActivity activity, Servo servo) {
        super(activity, Servo.newInstance(servo));
    }

    @Override
    public void start() {
        startDialog(ChooseRelationshipServoDialogWizard.newInstance(this));
    }


    public void generateSettings(Servo output) {
        if (currentState.relationshipType.getClass() == NoRelationship.class) {
            currentState.relationshipType = Proportional.getInstance();
        }

        SettingsProportional newSettings = SettingsProportional.newInstance(output.getSettings());
        newSettings.setSensorPortNumber(currentState.selectedSensorPort);
        newSettings.setOutputMin(currentState.outputMin);
        newSettings.setOutputMax(currentState.outputMax);
        output.setSettings(Settings.newInstance(newSettings, currentState.relationshipType));
        output.setIsLinked(true, output);
    }


    public BaseOutputDialog generateOutputDialog(Servo output, RobotActivity activity) {
        return ServoDialog.newInstance(output, activity);
    }

}
