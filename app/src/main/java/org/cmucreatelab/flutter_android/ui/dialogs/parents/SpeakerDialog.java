package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/17/2016.
 *
 * SpeakerDialog
 *
 * A Dialog that shows the options for creating a link between Speaker and a Sensor
 */
public class SpeakerDialog extends BaseResizableDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        MaxVolumeDialog.DialogMaxVolumeListener,
        MinVolumeDialog.DialogMinVolumeListener,
        MaxPitchDialog.DialogMaxPitchListener,
        MinPitchDialog.DialogMinPitchListener {


    private DialogSpeakerListener dialogSpeakerListener;

    private Serializable serializable;
    private DialogFragment dialogFragment;
    private boolean isVolume;

    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;
    private Button buttonVolume;
    private Button buttonPitch;
    private RelativeLayout relativeVolume;
    private RelativeLayout relativePitch;
    private Button saveButton;

    private Settings pitchSettings;
    private Settings volumeSettings;
    private Speaker speaker;


    private void updateViews(View view) {
        pitchSettings = speaker.getPitch().getSettings();
        volumeSettings = speaker.getVolume().getSettings();
        updateViews(view, speaker.getPitch());

        // max Volume and max Pitch
        ImageView maxVolumeImg = (ImageView) view.findViewById(R.id.image_max_volume);
        ImageView maxPitch = (ImageView) view.findViewById(R.id.image_max_pitch);
        maxVolumeImg.setImageResource(R.drawable.link_icon_volume_high);
        maxPitch.setImageResource(R.drawable.link_icon_pitch);
        TextView maxVolumeTxt = (TextView) view.findViewById(R.id.text_max_volume);
        TextView maxPitchTxt = (TextView) view.findViewById(R.id.text_max_pitch);
        maxVolumeTxt.setText(getString(volumeSettings.getSensor().getHighTextId()) + " " + getString(R.string.volume));
        maxPitchTxt.setText(getString(pitchSettings.getSensor().getHighTextId()) + " " + getString(R.string.pitch));
        TextView maxVolumeValue = (TextView) view.findViewById(R.id.text_max_volume_value);
        TextView maxPitchValue = (TextView) view.findViewById(R.id.text_max_pitch_value);
        maxVolumeValue.setText(String.valueOf(volumeSettings.getOutputMax()));
        maxPitchValue.setText(String.valueOf(pitchSettings.getOutputMax()) + " " + getString(R.string.hz));

        //min Volume and min Pitch
        ImageView minVolumeImg = (ImageView) view.findViewById(R.id.image_min_volume);
        ImageView minPitch = (ImageView) view.findViewById(R.id.image_min_pitch);
        minVolumeImg.setImageResource(R.drawable.link_icon_volume_low);
        minPitch.setImageResource(R.drawable.link_icon_pitch);
        TextView minVolumeTxt = (TextView) view.findViewById(R.id.text_min_volume);
        TextView minPitchTxt = (TextView) view.findViewById(R.id.text_min_pitch);
        minVolumeTxt.setText(getString(volumeSettings.getSensor().getLowTextId()) + " " + getString(R.string.volume));
        minPitchTxt.setText(getString(pitchSettings.getSensor().getLowTextId()) + " " + getString(R.string.pitch));
        TextView minVolumeValue = (TextView) view.findViewById(R.id.text_min_volume_value);
        TextView minPitchValue = (TextView) view.findViewById(R.id.text_min_pitch_value);
        minVolumeValue.setText(String.valueOf(volumeSettings.getOutputMin()));
        minPitchValue.setText(String.valueOf(pitchSettings.getOutputMin()) + " " + getString(R.string.hz));
    }


    public static SpeakerDialog newInstance(Speaker speaker, Serializable activity) {
        SpeakerDialog ledDialog = new SpeakerDialog();

        Bundle args = new Bundle();
        args.putSerializable(Speaker.SPEAKER_KEY, speaker);
        args.putSerializable(Constants.SERIALIZABLE_KEY, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        serializable = this;
        dialogFragment = this;
        isVolume = true;

        speaker = (Speaker) getArguments().getSerializable(Speaker.SPEAKER_KEY);
        dialogSpeakerListener = (DialogSpeakerListener) getArguments().getSerializable(Constants.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_speakers, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ButterKnife.bind(this, view);

        buttonVolume = (Button) view.findViewById(R.id.button_volume);
        buttonPitch = (Button) view.findViewById(R.id.button_pitch);
        relativeVolume = (RelativeLayout) view.findViewById(R.id.relative_volume);
        relativePitch = (RelativeLayout) view.findViewById(R.id.relative_pitch);

        pitchSettings = speaker.getPitch().getSettings();
        volumeSettings = speaker.getVolume().getSettings();

        updateViews(view);
        saveButton = (Button) view.findViewById(R.id.button_save_settings);

        return builder.create();
    }


    // onClick Listeners


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, speaker);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.button_save_settings)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");
        ArrayList<String> msgs = new ArrayList<>();
        msgs.add(MessageConstructor.getRemoveLinkMessage(speaker.getPitch()));
        msgs.add(MessageConstructor.getRemoveLinkMessage(speaker.getVolume()));
        msgs.add(MessageConstructor.getLinkedMessage(speaker.getVolume()));
        msgs.add(MessageConstructor.getLinkedMessage(speaker.getPitch()));
        speaker.getVolume().setIsLinked(true, speaker.getVolume());
        speaker.getPitch().setIsLinked(true, speaker.getPitch());
        dialogSpeakerListener.onSpeakerLinkListener(msgs);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");
        ArrayList<String> msgs = new ArrayList<>();
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");
        msgs.add(MessageConstructor.getRemoveLinkMessage(speaker.getPitch()));
        msgs.add(MessageConstructor.getRemoveLinkMessage(speaker.getVolume()));
        speaker.getPitch().setIsLinked(false, speaker.getPitch());
        speaker.getVolume().setIsLinked(false, speaker.getVolume());
        volumeSettings.setOutputMax(speaker.getVolume().getMax());
        volumeSettings.setOutputMin(speaker.getVolume().getMin());
        pitchSettings.setOutputMax(speaker.getPitch().getMax());
        pitchSettings.setOutputMin(speaker.getPitch().getMin());
        dialogSpeakerListener.onSpeakerLinkListener(msgs);
        this.dismiss();
    }


    @OnClick(R.id.button_volume)
    public void onClickVolume() {
        Log.d(Constants.LOG_TAG, "onClickVolume");
        if (!isVolume) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_left));
            buttonVolume.setTextColor(Color.WHITE);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_right));
            buttonPitch.setTextColor(Color.GRAY);
            relativePitch.setVisibility(View.GONE);
            relativeVolume.setVisibility(View.VISIBLE);
            isVolume = true;
        }
    }


    @OnClick(R.id.button_pitch)
    public void onClickPitch() {
        Log.d(Constants.LOG_TAG, "onClickPitch");
        if (isVolume) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_left));
            buttonVolume.setTextColor(Color.GRAY);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_right));
            buttonPitch.setTextColor(Color.WHITE);
            relativePitch.setVisibility(View.VISIBLE);
            relativeVolume.setVisibility(View.GONE);
            isVolume = false;
        }
    }


    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = SensorOutputDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = RelationshipOutputDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_volume)
    public void onClickSetMaxVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxVolume");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MaxVolumeDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_volume)
    public void onClickSetMinVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinVolume");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MinVolumeDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_pitch)
    public void onClickSetMaxPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxPitch");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MaxPitchDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_pitch)
    public void onClickSetMinPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPitch");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MinPitchDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    // Option Listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        volumeSettings.setAdvancedSettings(advancedSettings);
        pitchSettings.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != Sensor.Type.NO_SENSOR) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorType().toString());
            volumeSettings.setSensor(sensor);
            pitchSettings.setSensor(sensor);
            saveButton.setEnabled(true);
        }
    }

    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());
        volumeSettings.setRelationship(relationship);
        pitchSettings.setRelationship(relationship);
    }


    @Override
    public void onMaxVolumeChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxVolumeChosen");
        currentTextViewDescrp.setText(getString(volumeSettings.getSensor().getHighTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(max));
        currentImageView.setImageResource(R.drawable.link_icon_volume_high);
        volumeSettings.setOutputMax(max);
    }


    @Override
    public void onMinVolumeChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(volumeSettings.getSensor().getLowTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(min));
        currentImageView.setImageResource(R.drawable.link_icon_volume_low);
        volumeSettings.setOutputMin(min);
    }


    @Override
    public void onMaxPitchChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(pitchSettings.getSensor().getHighTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(max) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        pitchSettings.setOutputMax(max);
    }


    @Override
    public void onMinPitchChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(pitchSettings.getSensor().getLowTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(min) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        pitchSettings.setOutputMin(min);
    }


    public interface DialogSpeakerListener {
        public void onSpeakerLinkListener(ArrayList<String> msgs);
    }

}
