package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.speaker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Pitch;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.ChoosePitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv on 6/28/18.
 */

public class ChoosePitchSpeakerDialogWizard extends ChoosePitchDialog {

    SpeakerWizard.SpeakerWizardState wizardState;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

    public static final String DIALOG_TYPE = "dialog_type";

    public OutputWizard wizard;
    public static String KEY_WIZARD = "key_wizard";


    public enum OUTPUT_TYPE {
        MIN, MAX;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(400), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void updateWizardState() {
        wizardState = (SpeakerWizard.SpeakerWizardState) (wizard.getCurrentState());
    }


    public static ChoosePitchSpeakerDialogWizard newInstance(OutputWizard wizard, OUTPUT_TYPE type) {
        Bundle args = new Bundle();
        ChoosePitchSpeakerDialogWizard dialogWizard = new ChoosePitchSpeakerDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(DIALOG_TYPE, type);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_pitch_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        currentNote = (TextView) view.findViewById(R.id.text_current_note);
        currentPitch = (TextView) view.findViewById(R.id.text_current_pitch);
        sheetMusic = (ImageView) view.findViewById(R.id.image_sheet_music);
        seekBarPitch = (SeekBar) view.findViewById(R.id.seek_pitch);
        seekBarPitch.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        seekBarPitch.setOnSeekBarChangeListener(seekBarChangeListener);

        this.wizard = (OutputWizard) (getArguments().getSerializable(KEY_WIZARD));

        this.outputType = (OUTPUT_TYPE) (getArguments().getSerializable(DIALOG_TYPE));

        view.findViewById(R.id.button_next).setBackgroundResource(R.drawable.round_green_button_bottom_right);

        view.findViewById(R.id.button_set_pitch).setVisibility(View.GONE);

        updateWizardState();

        updateViewWithOptions();

        updateTextAndAudio(view);

        builder.setView(view);
        ButterKnife.bind(this, view);

        return builder.create();
    }

    private void updateViewWithOptions() {
        //start off at 0 for constant relationships
        if (wizardState.pitchRelationshipType instanceof Constant && wizardState.pitchMax == Pitch.MAXIMUM) {
            wizardState.pitchMax = Pitch.MINIMUM;
        } else if (wizardState.pitchMax == Pitch.MINIMUM) {
            wizardState.pitchMax = Pitch.MAXIMUM;
        }

        if (outputType.equals(OUTPUT_TYPE.MIN)) {
            populateViewWithPitch(wizardState.pitchMin);
        } else {
            populateViewWithPitch(wizardState.pitchMax);
        }
    }


    private void updateTextAndAudio(View view) {
        // views
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_pitch_speaker));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.link_icon_pitch);
        ((TextView) view.findViewById(R.id.text_set_pitch)).setText(getPositionPrompt());

        flutterAudioPlayer.addAudio(R.raw.audio_21);
        flutterAudioPlayer.playAudio();
    }


    private String getPositionPrompt() {
        if (!(wizardState.pitchRelationshipType instanceof Constant)) {
            Sensor[] sensors = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();

            switch (outputType) {
                case MIN:
                    return "Set the " + getString(sensors[wizardState.selectedSensorPortPitch - 1].getLowTextId()).toLowerCase() + " pitch";
                default:
                    return "Set the " + getString(sensors[wizardState.selectedSensorPortPitch - 1].getHighTextId()).toLowerCase() + " pitch";
            }
        } else {
            return "Set the constant pitch";
        }
    }


    @OnClick(R.id.button_back)
    public void onClickBack() {
        if (outputType.equals(OUTPUT_TYPE.MIN)) {
            wizardState.pitchMin = finalPitch;
            wizard.changeDialog(ChooseSensorSpeakerDialogWizard.newInstance(wizard, SpeakerType.PITCH));
        } else {
            wizardState.pitchMax = finalPitch;
            if (!wizardState.speakerWizardType.equals(SpeakerWizardType.VOLUME)) {
                if (wizardState.pitchRelationshipType instanceof Constant) {
                    wizard.changeDialog(ChooseRelationshipSpeakerDialogWizard.newInstance(wizard, SpeakerType.PITCH));
                } else {
                    wizard.changeDialog(ChoosePitchSpeakerDialogWizard.newInstance(wizard, ChoosePitchSpeakerDialogWizard.OUTPUT_TYPE.MIN));
                }
            }
            else
                wizard.changeDialog(ChooseVolumeSpeakerDialogWizard.newInstance(wizard, ChooseVolumeSpeakerDialogWizard.OUTPUT_TYPE.MAX));
        }
    }


    @OnClick(R.id.button_next)
    public void onClickNext() {
        if (outputType.equals(OUTPUT_TYPE.MIN)) {
            wizardState.pitchMin = finalPitch;
            wizard.changeDialog(ChoosePitchSpeakerDialogWizard.newInstance(wizard, ChoosePitchSpeakerDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizardState.pitchMax = finalPitch;
            if (wizardState.speakerWizardType.equals(SpeakerWizardType.PITCH)) {
                wizard.changeDialog(ChooseVolumeSpeakerDialogWizard.newInstance(wizard, ChooseVolumeSpeakerDialogWizard.OUTPUT_TYPE.MAX));
            }
            else {
                wizard.finish();
            }
        }
    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.i(Constants.LOG_TAG, "onClickAdvancedSettings");
        wizard.finish();
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }
}
