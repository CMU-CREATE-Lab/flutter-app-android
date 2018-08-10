package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ChooseRelationshipOutputDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.OnClick;

import static org.cmucreatelab.flutter_android.helpers.static_classes.Constants.LOG_TAG;

/**
 * Created by Parv on 6/27/18.
 *
 * ChooseRelationshipSpeakerDialogWizard
 *
 * A class for choosing the relationship for the speaker.
 */
public class ChooseRelationshipSpeakerDialogWizard extends ChooseRelationshipOutputDialogWizard {

    SpeakerWizard.SpeakerWizardState wizardState;
    private static final String SPEAKER_TYPE = "speaker_type";
    private SpeakerType speakerType;


    public static ChooseRelationshipSpeakerDialogWizard newInstance(OutputWizard wizard, SpeakerType speakerType) {
        Bundle args = new Bundle();
        ChooseRelationshipSpeakerDialogWizard dialogWizard = new ChooseRelationshipSpeakerDialogWizard();
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
            selectedView = getViewFromRelationship(wizardState.volumeRelationshipType);
        } else {
            selectedView = getViewFromRelationship(wizardState.pitchRelationshipType);
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


    public void updateRelationshipType(View view) {
        if (speakerType.equals(SpeakerType.VOLUME)) {
            wizardState.volumeRelationshipType = getRelationshipFromId(view.getId());
        } else {
            wizardState.pitchRelationshipType = getRelationshipFromId(view.getId());
        }
    }


    public void updateTextAndAudio(View view) {
        if (speakerType.equals(SpeakerType.VOLUME)) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_volume_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_volume_high);
            ((TextView) view.findViewById(R.id.text_relationship_prompt)).setText(getString(R.string.volume_speaker_relationship_prompt));
            flutterAudioPlayer.addAudio(R.raw.audio_15);
            flutterAudioPlayer.playAudio();
        } else {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_pitch_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_pitch);
            ((TextView) view.findViewById(R.id.text_relationship_prompt)).setText(getString(R.string.pitch_speaker_relationship_prompt));
            flutterAudioPlayer.addAudio(R.raw.audio_19);
            flutterAudioPlayer.playAudio();
        }

        ((Button) view.findViewById(R.id.button_cancel)).setText(getString(R.string.back));
    }


    public void updateWizardState() {
        wizardState = (SpeakerWizard.SpeakerWizardState) (wizard.getCurrentState());
    }


    @Override
    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        if (wizardState.speakerWizardType.equals(SpeakerWizardType.BOTH)) {
            if (speakerType.equals(SpeakerType.VOLUME)) {
                wizard.changeDialog(ExplanationSpeakerDialogWizard.newInstance(wizard, SpeakerType.VOLUME));
            } else {
                wizard.changeDialog(ExplanationSpeakerDialogWizard.newInstance(wizard, SpeakerType.PITCH));
            }
        } else {
            wizard.changeDialog(ChooseSpeakerTypeDialogWizard.newInstance(wizard));
        }
    }


    public void onClickNext() {
        Log.d(LOG_TAG, "onClickNext() called");

        if (speakerType.equals(SpeakerType.VOLUME)) {
            if (getViewFromRelationship(wizardState.volumeRelationshipType) != null) {
                if (wizardState.volumeRelationshipType instanceof Constant) {
                    wizard.changeDialog(ChooseVolumeSpeakerDialogWizard.newInstance(wizard, ChooseVolumeSpeakerDialogWizard.OUTPUT_TYPE.MAX));
                } else {
                    wizard.changeDialog(ChooseSensorSpeakerDialogWizard.newInstance(wizard, SpeakerType.VOLUME));
                }
            }
        } else {
            if (getViewFromRelationship(wizardState.pitchRelationshipType) != null) {
                if (wizardState.pitchRelationshipType instanceof Constant) {
                    wizard.changeDialog(ChoosePitchSpeakerDialogWizard.newInstance(wizard, ChoosePitchSpeakerDialogWizard.OUTPUT_TYPE.MAX));
                } else {
                    wizard.changeDialog(ChooseSensorSpeakerDialogWizard.newInstance(wizard, SpeakerType.PITCH));
                }
            }
        }
    }
}
