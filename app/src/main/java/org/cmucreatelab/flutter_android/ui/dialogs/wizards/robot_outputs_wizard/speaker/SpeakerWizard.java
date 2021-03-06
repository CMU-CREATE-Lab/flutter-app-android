package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Pitch;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker.SpeakerDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

/**
 * Created by Parv on 7/10/18.
 *
 * SpeakerWizard
 *
 * A class to store the parts of the speaker wizard.
 */
public class SpeakerWizard extends OutputWizard<Speaker> {

    private SpeakerWizardState currentState;

    public class SpeakerWizardState extends State {
        public SpeakerWizardType speakerWizardType = null;
        public Relationship volumeRelationshipType = NoRelationship.getInstance();
        public Relationship pitchRelationshipType = NoRelationship.getInstance();
        public int selectedSensorPortVolume = 0, selectedSensorPortPitch = 0;
		public int volumeMin = 0, pitchMin = Pitch.MINIMUM;
        public int volumeMax = 100, pitchMax = Pitch.MAXIMUM;
    }


    @Override
    public void createState() {
        currentState = new SpeakerWizardState();
    }


    @Override
    public State getCurrentState() {
        return currentState;
    }


    public SpeakerWizard(RobotActivity activity, Speaker speaker) {
        super(activity, Speaker.newInstance(speaker));
    }

	@Override
	public void start() {
		startDialog(ChooseSpeakerTypeDialogWizard.newInstance(this));
	}

    public void generateSettings(Speaker output) {
        if (currentState.volumeRelationshipType.getClass() == NoRelationship.class) {
            currentState.volumeRelationshipType = Proportional.getInstance();
        }
        if (currentState.pitchRelationshipType.getClass() == NoRelationship.class) {
            currentState.pitchRelationshipType = Proportional.getInstance();
        }

        SettingsProportional newVolumeSettings = SettingsProportional.newInstance(output.getVolume().getSettings());
        newVolumeSettings.setSensorPortNumber(currentState.selectedSensorPortVolume);
        newVolumeSettings.setOutputMin(currentState.volumeMin);
        newVolumeSettings.setOutputMax(currentState.volumeMax);
        output.getVolume().setSettings(Settings.newInstance(newVolumeSettings, currentState.volumeRelationshipType));

        SettingsProportional newPitchSettings = SettingsProportional.newInstance(output.getPitch().getSettings());
        newPitchSettings.setSensorPortNumber(currentState.selectedSensorPortPitch);
        newPitchSettings.setOutputMin(currentState.pitchMin);
        newPitchSettings.setOutputMax(currentState.pitchMax);
        output.getPitch().setSettings(Settings.newInstance(newPitchSettings, currentState.pitchRelationshipType));

        output.getVolume().setIsLinked(true, output.getVolume());
        output.getPitch().setIsLinked(true, output.getPitch());
    }


    public BaseOutputDialog generateOutputDialog(Speaker output, RobotActivity activity) {
        return SpeakerDialog.newInstance(output, activity);
    }

}
