package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.led;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led.LedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChooseRelationshipServoDialogWizard;

/**
 * Created by Parv on 7/10/18.
 */

public class LedWizard extends OutputWizard<TriColorLed> {

    private LedWizardState currentState;

    public class LedWizardState extends State {
        public Relationship relationshipType = NoRelationship.getInstance();
        public int selectedSensorPort = 0;
		public Integer[] outputsMin = new Integer[]{255, 255, 255};
        public Integer[] outputsMax = new Integer[]{0, 0, 0};
    }


    @Override
    public void createState() {
        currentState = new LedWizardState();
    }


    @Override
    public State getCurrentState() {
        return currentState;
    }


    public LedWizard(RobotActivity activity, TriColorLed led) {
        super(activity, TriColorLed.newInstance(led));
    }

	@Override
	public void start() {
		startDialog(ChooseRelationshipLedDialogWizard.newInstance(this));
	}

    public void generateSettings(TriColorLed output) {
        // TODO @tasota avoid crash when something wasn't set
        if (currentState.relationshipType.getClass() == NoRelationship.class) {
            currentState.relationshipType = Proportional.getInstance();
        }
        if (currentState.selectedSensorPort < 1 || currentState.selectedSensorPort > 3) {
            currentState.selectedSensorPort = 1;
        }

		output.setSensorPortNumber(currentState.selectedSensorPort);

		output.setOutputMin(currentState.outputsMin[0], currentState.outputsMin[1], currentState.outputsMin[2]);

		output.setOutputMax(currentState.outputsMax[0], currentState.outputsMax[1], currentState.outputsMax[2]);

		output.getRedLed().setSettings(Settings.newInstance(output.getRedLed().getSettings(), currentState.relationshipType));
		output.getGreenLed().setSettings(Settings.newInstance(output.getGreenLed().getSettings(), currentState.relationshipType));
		output.getBlueLed().setSettings(Settings.newInstance(output.getBlueLed().getSettings(), currentState.relationshipType));

		output.getRedLed().setIsLinked(true, output.getRedLed());
		output.getGreenLed().setIsLinked(true, output.getGreenLed());
		output.getBlueLed().setIsLinked(true, output.getBlueLed());
    }


    public BaseOutputDialog generateOutputDialog(TriColorLed output, RobotActivity activity) {
        return LedDialog.newInstance(output, activity);
    }

}
