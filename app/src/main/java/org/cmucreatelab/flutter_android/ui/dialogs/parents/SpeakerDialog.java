package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.content.DialogInterface;
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
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
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
public class SpeakerDialog extends BaseResizableDialog implements Serializable, DialogInterface.OnClickListener,
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

    private Settings pitchSettings;
    private Settings volumeSettings;
    private Speaker speaker;


    public static SpeakerDialog newInstance(Speaker speaker, Serializable activity) {
        SpeakerDialog ledDialog = new SpeakerDialog();

        Bundle args = new Bundle();
        args.putSerializable(Speaker.SPEAKER_KEY, speaker);
        args.putSerializable(RobotActivity.SERIALIZABLE_KEY, activity);
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
        dialogSpeakerListener = (DialogSpeakerListener) getArguments().getSerializable(RobotActivity.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_speakers, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        builder.setPositiveButton(R.string.save_settings, this);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ButterKnife.bind(this, view);

        buttonVolume = (Button) view.findViewById(R.id.button_volume);
        buttonPitch = (Button) view.findViewById(R.id.button_pitch);
        relativeVolume = (RelativeLayout) view.findViewById(R.id.relative_volume);
        relativePitch = (RelativeLayout) view.findViewById(R.id.relative_pitch);

        pitchSettings = new Settings("f");
        volumeSettings = new Settings("v");
        speaker.setFrequencySettings(pitchSettings);
        speaker.setVolumeSettings(volumeSettings);

        return builder.create();
    }


    // onClick Listeners


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSave");
        ArrayList<String> msgs = new ArrayList<>();
        speaker.setSettings(volumeSettings);
        msgs.add(MessageConstructor.getLinkedMessage(speaker));
        speaker.setSettings(pitchSettings);
        msgs.add(MessageConstructor.getLinkedMessage(speaker));
        dialogSpeakerListener.onSpeakerLinkListener(msgs);
    }


    @OnClick(R.id.button_volume)
    public void onClickVolume() {
        Log.d(Constants.LOG_TAG, "onClickVolume");
        if (!isVolume) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_left));
            buttonVolume.setTextColor(Color.WHITE);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_white_right));
            buttonPitch.setTextColor(Color.BLACK);
            relativePitch.setVisibility(View.INVISIBLE);
            relativeVolume.setVisibility(View.VISIBLE);
            isVolume = true;


        }
    }


    @OnClick(R.id.button_pitch)
    public void onClickPitch() {
        Log.d(Constants.LOG_TAG, "onClickPitch");
        if (isVolume) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_white_left));
            buttonVolume.setTextColor(Color.BLACK);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_right));
            buttonPitch.setTextColor(Color.WHITE);
            relativePitch.setVisibility(View.VISIBLE);
            relativeVolume.setVisibility(View.INVISIBLE);
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


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen");
        currentImageView.setImageResource(sensor.getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(sensor.getSensorType().toString());
        volumeSettings.setSensor(sensor);
        pitchSettings.setSensor(sensor);
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
        currentTextViewDescrp.setText(getString(pitchSettings.getSensor().getHighTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(max));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        pitchSettings.setOutputMax(max);
    }


    @Override
    public void onMinPitchChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(pitchSettings.getSensor().getLowTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(min));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        pitchSettings.setOutputMin(min);
    }


    public interface DialogSpeakerListener {
        public void onSpeakerLinkListener(ArrayList<String> msgs);
    }

}
