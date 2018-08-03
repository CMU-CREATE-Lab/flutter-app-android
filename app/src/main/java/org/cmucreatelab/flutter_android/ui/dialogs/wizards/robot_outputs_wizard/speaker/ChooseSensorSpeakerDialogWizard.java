package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseSensorOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by Parv on 7/10/18.
 */

public class ChooseSensorSpeakerDialogWizard extends ChooseSensorOutputDialogWizard {
    SpeakerWizard.SpeakerWizardState wizardState;
    private static final String SPEAKER_TYPE = "speaker_type";
    private SpeakerType speakerType;


    public static ChooseSensorSpeakerDialogWizard newInstance(OutputWizard wizard, SpeakerType speakerType) {
        Bundle args = new Bundle();
        ChooseSensorSpeakerDialogWizard dialogWizard = new ChooseSensorSpeakerDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(SPEAKER_TYPE, speakerType);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.speakerType = (SpeakerType) (getArguments().getSerializable(SPEAKER_TYPE));
        return super.onCreateDialog(savedInstanceState);
    }


    public void updateViewWithOptions() {
        View selectedView;
        if (speakerType.equals(SpeakerType.VOLUME)) {
            selectedView = getViewFromSensorPort(wizardState.selectedSensorPortVolume);
        } else {
            selectedView = getViewFromSensorPort(wizardState.selectedSensorPortPitch);
        }

        if (selectedView != null) {
            nextButton.setClickable(true);
            nextButton.setBackgroundResource(R.drawable.round_green_button_bottom_right);
            selectedView(selectedView);
        } else {
            nextButton.setClickable(false);
            clearSelection();
        }
    }


    public void updateSelectedSensorPort(View view) {
        if (speakerType.equals(SpeakerType.VOLUME)) {
            wizardState.selectedSensorPortVolume = getSensorPortFromId(view.getId());
        } else {
            wizardState.selectedSensorPortPitch = getSensorPortFromId(view.getId());
        }
    }


    public void updateTextAndAudio(View view) {
        if (speakerType.equals(SpeakerType.VOLUME)) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_volume_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_volume_high);
            ((TextView) view.findViewById(R.id.text_sensor_prompt)).setText(getString(R.string.volume_speaker_sensor_prompt));
            //TODO: Need to add audio file for this one
        } else {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_pitch_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_pitch);
            ((TextView) view.findViewById(R.id.text_sensor_prompt)).setText(getString(R.string.pitch_speaker_sensor_prompt));
            flutterAudioPlayer.addAudio(R.raw.audio_20);
            flutterAudioPlayer.playAudio();
        }
    }


    public void onClickBack() {
        wizard.changeDialog(ChooseRelationshipSpeakerDialogWizard.newInstance(wizard, speakerType));
    }


    public void updateWizardState() {
        wizardState = (SpeakerWizard.SpeakerWizardState) (wizard.getCurrentState());
    }


    public void onClickNext() {
        Log.d(LOG_TAG, "onClickNext() called");
        if (speakerType.equals(SpeakerType.VOLUME)) {
            if (getViewFromSensorPort(wizardState.selectedSensorPortVolume) != null) {
                wizard.changeDialog(ChooseVolumeSpeakerDialogWizard.newInstance(wizard, ChooseVolumeSpeakerDialogWizard.OUTPUT_TYPE.MIN));
            }
        } else {
            if (getViewFromSensorPort(wizardState.selectedSensorPortPitch) != null) {
                wizard.changeDialog(ChoosePitchSpeakerDialogWizard.newInstance(wizard, ChoosePitchSpeakerDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }
}
