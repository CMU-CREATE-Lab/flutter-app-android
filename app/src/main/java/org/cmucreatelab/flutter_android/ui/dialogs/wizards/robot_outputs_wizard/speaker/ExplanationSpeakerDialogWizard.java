package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv on 6/28/18.
 */

public class ExplanationSpeakerDialogWizard extends BaseResizableDialogWizard {

    SpeakerWizard.SpeakerWizardState wizardState;
    private static final String SPEAKER_TYPE = "speaker_type";
    private SpeakerType speakerType;


    public static ExplanationSpeakerDialogWizard newInstance(OutputWizard wizard, SpeakerType speakerType) {
        Bundle args = new Bundle();
        ExplanationSpeakerDialogWizard dialogWizard = new ExplanationSpeakerDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(SPEAKER_TYPE, speakerType);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_explanation_speaker_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.speakerType = (SpeakerType) (getArguments().getSerializable(SPEAKER_TYPE));

        view.findViewById(R.id.button_next).setBackgroundResource(R.drawable.round_green_button_bottom_right);

        updateWizardState();

        updateTextAndAudio(view);

        return builder.create();
    }


    private void updateTextAndAudio(View view) {
        if (speakerType.equals(SpeakerType.VOLUME)) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_volume_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_volume_high);
            ((TextView) view.findViewById(R.id.text_speaker_explanation)).setText(getString(R.string.create_link_volume));
            flutterAudioPlayer.addAudio(R.raw.a_14);
            flutterAudioPlayer.playAudio();
        } else {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_pitch_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_pitch);
            ((TextView) view.findViewById(R.id.text_speaker_explanation)).setText(getString(R.string.create_link_pitch));
            flutterAudioPlayer.addAudio(R.raw.a_18);
            flutterAudioPlayer.playAudio();
        }
    }


    public void updateWizardState() {
        wizardState = (SpeakerWizard.SpeakerWizardState) (wizard.getCurrentState());
    }


    @OnClick(R.id.button_next)
    public void onClickNext() {
        wizard.changeDialog(ChooseRelationshipSpeakerDialogWizard.newInstance(wizard, speakerType));
    }


    @OnClick(R.id.button_back)
    public void onClickBack() {
        if (speakerType.equals(SpeakerType.VOLUME))
            wizard.changeDialog(ChooseSpeakerTypeDialogWizard.newInstance(wizard));
        else
            wizard.changeDialog(ChooseVolumeSpeakerDialogWizard.newInstance(wizard, ChooseVolumeSpeakerDialogWizard.OUTPUT_TYPE.MAX));
    }
}
