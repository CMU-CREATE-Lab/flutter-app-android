package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv on 6/28/18.
 */

public class ChooseSpeakerTypeDialogWizard extends BaseResizableDialogWizard {

    SpeakerWizard.SpeakerWizardState wizardState;


    public static ChooseSpeakerTypeDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseSpeakerTypeDialogWizard dialogWizard = new ChooseSpeakerTypeDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_speaker_type_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        updateWizardState();

        updateTextViews(view);

        return builder.create();
    }


    private void updateTextViews(View view) {
        // views
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
    }

    public void updateWizardState() {
        wizardState = (SpeakerWizard.SpeakerWizardState) (wizard.getCurrentState());
    }

    @OnClick(R.id.button_both)
    public void onClickBoth() {
        wizardState.speakerWizardType = SpeakerWizardType.BOTH;
        wizardState.volumeRelationshipType = NoRelationship.getInstance();
        wizardState.pitchRelationshipType = NoRelationship.getInstance();
        wizard.changeDialog(ExplanationSpeakerDialogWizard.newInstance(wizard, SpeakerType.VOLUME));
    }


    @OnClick(R.id.button_pitch)
    public void onClickPitch() {
        wizardState.speakerWizardType = SpeakerWizardType.PITCH;
        wizardState.pitchRelationshipType = NoRelationship.getInstance();
        wizardState.volumeRelationshipType = Constant.getInstance();
        wizard.changeDialog(ChooseRelationshipSpeakerDialogWizard.newInstance(wizard, SpeakerType.PITCH));
    }


    @OnClick(R.id.button_volume)
    public void onClickVolume() {
        wizardState.speakerWizardType = SpeakerWizardType.VOLUME;
        wizardState.volumeRelationshipType = NoRelationship.getInstance();
        wizardState.pitchRelationshipType = Constant.getInstance();
        wizard.changeDialog(ChooseRelationshipSpeakerDialogWizard.newInstance(wizard, SpeakerType.VOLUME));
    }




    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.i(Constants.LOG_TAG, "onClickAdvancedSettings");
        // TODO finish wizard, display summary/advanced dialog
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }
}
