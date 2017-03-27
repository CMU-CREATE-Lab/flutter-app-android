package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.BaseOutputDialog;

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
public class SpeakerDialog extends BaseOutputDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        MaxVolumeDialog.DialogMaxVolumeListener,
        MinVolumeDialog.DialogMinVolumeListener,
        MaxPitchDialog.DialogMaxPitchListener,
        MinPitchDialog.DialogMinPitchListener {

    private View dialogView;
    private DialogSpeakerListener dialogSpeakerListener;
    private TabType currentTab;
    private Speaker speaker;

    private LinearLayout linkedSensor,minVolumeLayout,minPitchLayout;
    private ImageView advancedSettingsView;
    private RelativeLayout relativeVolume;
    private RelativeLayout relativePitch;


    public enum TabType {
        VOLUME, PITCH
    }


    private void updatePitchViews(View view) {
        updateViews(view, speaker.getPitch());
        Relationship relationship = speaker.getPitch().getSettings().getRelationship();

        // Pitch
        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional pitchSettings = (SettingsProportional) speaker.getPitch().getSettings();
            // advanced settings
            advancedSettingsView.setVisibility(View.VISIBLE);

            // sensor
            linkedSensor.setVisibility(View.VISIBLE);
            ((ImageView)linkedSensor.getChildAt(0)).setImageResource(speaker.getPitch().getSettings().getSensor().getGreenImageId());
            ViewGroup sensorViewGroup = ((ViewGroup)linkedSensor.getChildAt(1));
            ((TextView)sensorViewGroup.getChildAt(0)).setText(R.string.linked_sensor);
            ((TextView)sensorViewGroup.getChildAt(1)).setText(speaker.getPitch().getSettings().getSensor().getSensorTypeId());

            // max Pitch
            ImageView maxPitch = (ImageView) view.findViewById(R.id.image_max_pitch);
            maxPitch.setImageResource(R.drawable.link_icon_pitch);
            TextView maxPitchTxt = (TextView) view.findViewById(R.id.text_max_pitch);
            maxPitchTxt.setText(getString(speaker.getPitch().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.pitch));
            TextView maxPitchValue = (TextView) view.findViewById(R.id.text_max_pitch_value);
            maxPitchValue.setText(String.valueOf(pitchSettings.getOutputMax()) + " " + getString(R.string.hz));

            // min Pitch
            minPitchLayout.setVisibility(View.VISIBLE);
            ImageView minPitch = (ImageView) view.findViewById(R.id.image_min_pitch);
            minPitch.setImageResource(R.drawable.link_icon_pitch);
            TextView minPitchTxt = (TextView) view.findViewById(R.id.text_min_pitch);
            minPitchTxt.setText(getString(speaker.getPitch().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.pitch));
            TextView minPitchValue = (TextView) view.findViewById(R.id.text_min_pitch_value);
            minPitchValue.setText(String.valueOf(pitchSettings.getOutputMin()) + " " + getString(R.string.hz));
        } else if (speaker.getPitch().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant pitchSettings = (SettingsConstant) speaker.getPitch().getSettings();

            // advanced settings
            advancedSettingsView.setVisibility(View.GONE);

            // sensor
            linkedSensor.setVisibility(View.GONE);

            // max Pitch
            ImageView maxPitch = (ImageView) view.findViewById(R.id.image_max_pitch);
            maxPitch.setImageResource(R.drawable.link_icon_pitch);
            TextView maxPitchTxt = (TextView) view.findViewById(R.id.text_max_pitch);
            maxPitchTxt.setText(getString(speaker.getPitch().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.pitch));
            TextView maxPitchValue = (TextView) view.findViewById(R.id.text_max_pitch_value);
            maxPitchValue.setText(String.valueOf(pitchSettings.getValue()) + " " + getString(R.string.hz));

            // min Pitch
            minPitchLayout.setVisibility(View.GONE);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.updatePitchViews: unimplemented relationship");
        }
    }


    private void updateVolumeViews(View view) {
        updateViews(view, speaker.getVolume());
        Relationship relationship = speaker.getVolume().getSettings().getRelationship();

        // Volume
        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional volumeSettings = (SettingsProportional) speaker.getVolume().getSettings();

            // advanced settings
            advancedSettingsView.setVisibility(View.VISIBLE);

            // sensor
            linkedSensor.setVisibility(View.VISIBLE);
            ((ImageView)linkedSensor.getChildAt(0)).setImageResource(speaker.getVolume().getSettings().getSensor().getGreenImageId());
            ViewGroup sensorViewGroup = ((ViewGroup)linkedSensor.getChildAt(1));
            ((TextView)sensorViewGroup.getChildAt(0)).setText(R.string.linked_sensor);
            ((TextView)sensorViewGroup.getChildAt(1)).setText(speaker.getVolume().getSettings().getSensor().getSensorTypeId());

            // max Volume
            ImageView maxVolumeImg = (ImageView) view.findViewById(R.id.image_max_volume);
            maxVolumeImg.setImageResource(R.drawable.link_icon_volume_high);
            TextView maxVolumeTxt = (TextView) view.findViewById(R.id.text_max_volume);
            maxVolumeTxt.setText(getString(speaker.getVolume().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.volume));
            TextView maxVolumeValue = (TextView) view.findViewById(R.id.text_max_volume_value);
            maxVolumeValue.setText(String.valueOf(volumeSettings.getOutputMax()));

            // min Volume
            minVolumeLayout.setVisibility(View.VISIBLE);
            ImageView minVolumeImg = (ImageView) view.findViewById(R.id.image_min_volume);
            minVolumeImg.setImageResource(R.drawable.link_icon_volume_low);
            TextView minVolumeTxt = (TextView) view.findViewById(R.id.text_min_volume);
            minVolumeTxt.setText(getString(speaker.getVolume().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.volume));
            TextView minVolumeValue = (TextView) view.findViewById(R.id.text_min_volume_value);
            minVolumeValue.setText(String.valueOf(volumeSettings.getOutputMin()));
        } else if (speaker.getVolume().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant volumeSettings = (SettingsConstant) speaker.getVolume().getSettings();

            // advanced settings
            advancedSettingsView.setVisibility(View.GONE);

            // sensor
            linkedSensor.setVisibility(View.GONE);

            // max Volume
            ImageView maxVolumeImg = (ImageView) view.findViewById(R.id.image_max_volume);
            maxVolumeImg.setImageResource(R.drawable.link_icon_volume_high);
            TextView maxVolumeTxt = (TextView) view.findViewById(R.id.text_max_volume);
            maxVolumeTxt.setText(getString(speaker.getVolume().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.volume));
            TextView maxVolumeValue = (TextView) view.findViewById(R.id.text_max_volume_value);
            maxVolumeValue.setText(String.valueOf(volumeSettings.getValue()));

            // min Volume
            minVolumeLayout.setVisibility(View.GONE);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.updateVolumeViews: unimplemented relationship");
        }
    }


    private void updateViews(View view) {
        switch (currentTab) {
            case VOLUME:
                updateVolumeViews(view);
                break;
            case PITCH:
                updatePitchViews(view);
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType.");
        }

        Button saveButton = (Button) view.findViewById(R.id.button_save_link);
        Button removeButton = (Button) view.findViewById(R.id.button_remove_link);

        saveButton.setEnabled(true);
        removeButton.setEnabled(true);
        if (!speaker.getVolume().getSettings().isSettable()) {
            saveButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
        if (!speaker.getPitch().getSettings().isSettable()) {
            saveButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }


    public static SpeakerDialog newInstance(Speaker speaker, Serializable activity) {
        SpeakerDialog ledDialog = new SpeakerDialog();

        Bundle args = new Bundle();
        args.putSerializable(Speaker.SPEAKER_KEY, speaker);
        args.putSerializable(Constants.SerializableKeys.DIALOG_SPEAKER, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    public TabType getCurrentTab() {
        return currentTab;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);
        currentTab = TabType.VOLUME;

        // clone old object
        speaker = Speaker.newInstance((Speaker) getArguments().getSerializable(Speaker.SPEAKER_KEY));

        dialogSpeakerListener = (DialogSpeakerListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_SPEAKER);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view= inflater.inflate(R.layout.dialog_speakers, null);
        this.dialogView = view;
        advancedSettingsView = (ImageView) dialogView.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) dialogView.findViewById(R.id.linear_set_linked_sensor);
        minPitchLayout = (LinearLayout) view.findViewById(R.id.linear_set_min_pitch);
        minVolumeLayout = (LinearLayout) view.findViewById(R.id.linear_set_min_volume);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
        ButterKnife.bind(this, view);

        relativeVolume = (RelativeLayout) view.findViewById(R.id.relative_volume);
        relativePitch = (RelativeLayout) view.findViewById(R.id.relative_pitch);

        updateViews(view);

        return builder.create();
    }


    // onClick Listeners


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog;

        switch (currentTab) {
            case VOLUME:
                dialog = AdvancedSettingsDialog.newInstance(this, speaker.getVolume());
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case PITCH:
                dialog = AdvancedSettingsDialog.newInstance(this, speaker.getPitch());
                dialog.show(this.getFragmentManager(), "tag");
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType.");
        }
    }


    @OnClick(R.id.button_save_link)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");
        ArrayList<MelodySmartMessage> msgs = new ArrayList<>();
        msgs.add(MessageConstructor.constructRemoveRelation(speaker.getPitch()));
        msgs.add(MessageConstructor.constructRemoveRelation(speaker.getVolume()));
        msgs.add(MessageConstructor.constructRelationshipMessage(speaker.getVolume(),speaker.getVolume().getSettings()));
        msgs.add(MessageConstructor.constructRelationshipMessage(speaker.getPitch(),speaker.getPitch().getSettings()));
        speaker.getVolume().setIsLinked(true, speaker.getVolume());
        speaker.getPitch().setIsLinked(true, speaker.getPitch());

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().setSpeaker(speaker);

        dialogSpeakerListener.onSpeakerLinkListener(msgs);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");
        ArrayList<MelodySmartMessage> msgs = new ArrayList<>();
        msgs.add(MessageConstructor.constructRemoveRelation(speaker.getPitch()));
        msgs.add(MessageConstructor.constructRemoveRelation(speaker.getVolume()));
        speaker.getPitch().setIsLinked(false, speaker.getPitch());
        speaker.getVolume().setIsLinked(false, speaker.getVolume());

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().setSpeaker(speaker);

        dialogSpeakerListener.onSpeakerLinkListener(msgs);
        this.dismiss();
    }


    @OnClick(R.id.button_volume)
    public void onClickVolume() {
        Log.d(Constants.LOG_TAG, "onClickVolume");
        Button buttonVolume = (Button) dialogView.findViewById(R.id.button_volume);
        Button buttonPitch = (Button) dialogView.findViewById(R.id.button_pitch);

        if (currentTab != TabType.VOLUME) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_left));
            buttonVolume.setTextColor(Color.WHITE);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_right));
            buttonPitch.setTextColor(Color.GRAY);
            relativePitch.setVisibility(View.GONE);
            relativeVolume.setVisibility(View.VISIBLE);
            currentTab = TabType.VOLUME;
            updateViews(dialogView);
        }
    }


    @OnClick(R.id.button_pitch)
    public void onClickPitch() {
        Log.d(Constants.LOG_TAG, "onClickPitch");
        Button buttonVolume = (Button) dialogView.findViewById(R.id.button_volume);
        Button buttonPitch = (Button) dialogView.findViewById(R.id.button_pitch);

        if (currentTab != TabType.PITCH) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_left));
            buttonVolume.setTextColor(Color.GRAY);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_right));
            buttonPitch.setTextColor(Color.WHITE);
            relativePitch.setVisibility(View.VISIBLE);
            relativeVolume.setVisibility(View.GONE);
            currentTab = TabType.PITCH;
            updateViews(dialogView);
        }
    }


    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
        DialogFragment dialog = SensorOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        DialogFragment dialog = RelationshipOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_volume)
    public void onClickSetMaxVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxVolume");

        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional volumeSettings = (SettingsProportional) speaker.getVolume().getSettings();
            DialogFragment dialog = MaxVolumeDialog.newInstance(volumeSettings.getOutputMax(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else if (speaker.getVolume().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant volumeSettings = (SettingsConstant) speaker.getVolume().getSettings();
            DialogFragment dialog = MaxVolumeDialog.newInstance(volumeSettings.getValue(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG, "SpeakerDialog.onClickSetMaxVolume: unimplemented relationship");
        }
    }


    @OnClick(R.id.linear_set_min_volume)
    public void onClickSetMinVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinVolume");

        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional volumeSettings = (SettingsProportional) speaker.getVolume().getSettings();
            DialogFragment dialog = MinVolumeDialog.newInstance(volumeSettings.getOutputMin(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG, "SpeakerDialog.onClickSetMaxVolume: unimplemented relationship");
        }
    }


    @OnClick(R.id.linear_set_max_pitch)
    public void onClickSetMaxPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxPitch");

        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional pitchSettings = (SettingsProportional) speaker.getPitch().getSettings();
            DialogFragment dialog = MaxPitchDialog.newInstance(pitchSettings.getOutputMax(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else if (speaker.getPitch().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant pitchSettings = (SettingsConstant) speaker.getPitch().getSettings();
            DialogFragment dialog = MaxPitchDialog.newInstance(pitchSettings.getValue(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG, "SpeakerDialog.onClickSetMaxPitch: unimplemented relationship");
        }
    }


    @OnClick(R.id.linear_set_min_pitch)
    public void onClickSetMinPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPitch");

        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional pitchSettings = (SettingsProportional) speaker.getPitch().getSettings();
            DialogFragment dialog = MinPitchDialog.newInstance(pitchSettings.getOutputMin(),this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG, "SpeakerDialog.onClickSetMinPitch: unimplemented relationship");
        }
    }


    // Option Listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        switch (currentTab) {
            case VOLUME:
                //speaker.getVolume().getSettings().setAdvancedSettings(advancedSettings);
                if (speaker.getVolume().getSettings().hasAdvancedSettings()) {
                    if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
                        SettingsProportional settingsVolume = (SettingsProportional) speaker.getVolume().getSettings();
                        settingsVolume.setAdvancedSettings(advancedSettings);
                    } else {
                        Log.e(Constants.LOG_TAG,"onAdvancedSettingsSet: unimplemented relationship for volume");
                    }
                } else {
                    Log.w(Constants.LOG_TAG,"Ignoring set advanced settings for Volume relationship.");
                }
                break;
            case PITCH:
                //speaker.getPitch().getSettings().setAdvancedSettings(advancedSettings);
                if (speaker.getPitch().getSettings().hasAdvancedSettings()) {
                    if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
                        SettingsProportional settingsPitch = (SettingsProportional) speaker.getPitch().getSettings();
                        settingsPitch.setAdvancedSettings(advancedSettings);
                    } else {
                        Log.e(Constants.LOG_TAG,"onAdvancedSettingsSet: unimplemented relationship for pitch");
                    }
                } else {
                    Log.w(Constants.LOG_TAG,"Ignoring set advanced settings for Pitch relationship.");
                }
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType");
        }
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");

            View view = dialogView.findViewById(R.id.linear_set_linked_sensor);
            ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
            View layout = ((ViewGroup) view).getChildAt(1);
            TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
            TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());
            // set sensor for the proper tab
            switch (currentTab) {
                case VOLUME:
                    if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
                        SettingsProportional settingsProportional = (SettingsProportional) speaker.getVolume().getSettings();
                        settingsProportional.setSensorPortNumber(sensor.getPortNumber());
                    } else {
                        Log.e(Constants.LOG_TAG,"SpeakerDialog.onSensorChosen: unimplemented relationship for Volume");
                    }
                    break;
                case PITCH:
                    if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
                        SettingsProportional settingsProportional = (SettingsProportional) speaker.getPitch().getSettings();
                        settingsProportional.setSensorPortNumber(sensor.getPortNumber());
                    } else {
                        Log.e(Constants.LOG_TAG,"SpeakerDialog.onSensorChosen: unimplemented relationship for Pitch");
                    }
                    break;
                default:
                    Log.e(Constants.LOG_TAG,"Failed to parse TabType.");
            }
            // set a sensor by default
            if (!speaker.getVolume().getSettings().isSettable()) {
                SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getVolume().getSettings());
                settingsConstant.setValue(100);
                speaker.getVolume().setSettings(settingsConstant);
            }
            if (!speaker.getPitch().getSettings().isSettable()) {
                SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getPitch().getSettings());
                settingsConstant.setValue(440);
                speaker.getPitch().setSettings(settingsConstant);
            }
        }
        updateViews(dialogView);
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");

        View view = dialogView.findViewById(R.id.linear_set_relationship);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());

        switch (currentTab) {
            case VOLUME:
                if (relationship.getClass() == Proportional.class) {
                    speaker.getVolume().setSettings(SettingsProportional.newInstance(speaker.getVolume().getSettings()));
                } else if (relationship.getClass() == Constant.class) {
                    speaker.getVolume().setSettings(SettingsConstant.newInstance(speaker.getVolume().getSettings()));
                } else {
                    Log.e(Constants.LOG_TAG,"SpeakerDialog.onRelationshipChosen: unimplemented relationship for Volume");
                }
                // set a sensor by default
                if (!speaker.getPitch().getSettings().isSettable()) {
                    SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getPitch().getSettings());
                    settingsConstant.setValue(440);
                    speaker.getPitch().setSettings(settingsConstant);
                }
                break;
            case PITCH:
                if (relationship.getClass() == Proportional.class) {
                    speaker.getPitch().setSettings(SettingsProportional.newInstance(speaker.getPitch().getSettings()));
                } else if (relationship.getClass() == Constant.class) {
                    speaker.getPitch().setSettings(SettingsConstant.newInstance(speaker.getPitch().getSettings()));
                } else {
                    Log.e(Constants.LOG_TAG,"SpeakerDialog.onRelationshipChosen: unimplemented relationship for Pitch");
                }
                // set a sensor by default
                if (!speaker.getVolume().getSettings().isSettable()) {
                    SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getVolume().getSettings());
                    settingsConstant.setValue(100);
                    speaker.getVolume().setSettings(settingsConstant);
                }
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType");
        }

        updateViews(dialogView);
    }


    @Override
    public void onMaxVolumeChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxVolumeChosen");

        View view = dialogView.findViewById(R.id.linear_set_max_volume);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentTextViewDescrp.setText(getString(speaker.getVolume().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(max));
        currentImageView.setImageResource(R.drawable.link_icon_volume_high);

        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settingsProportional = (SettingsProportional) speaker.getVolume().getSettings();
            settingsProportional.setOutputMax(max);
        } else if (speaker.getVolume().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settingsConstant = (SettingsConstant) speaker.getVolume().getSettings();
            settingsConstant.setValue(max);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.onMaxVolumeChosen: unimplemented relationship");
        }
    }


    @Override
    public void onMinVolumeChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");

        View view = dialogView.findViewById(R.id.linear_set_min_volume);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentTextViewDescrp.setText(getString(speaker.getVolume().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(min));
        currentImageView.setImageResource(R.drawable.link_icon_volume_low);

        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settingsProportional = (SettingsProportional) speaker.getVolume().getSettings();
            settingsProportional.setOutputMin(min);
        } else if (speaker.getVolume().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settingsConstant = (SettingsConstant) speaker.getVolume().getSettings();
            settingsConstant.setValue(min);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.onMinVolumeChosen: unimplemented relationship");
        }
    }


    @Override
    public void onMaxPitchChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");

        View view = dialogView.findViewById(R.id.linear_set_max_pitch);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentTextViewDescrp.setText(getString(speaker.getPitch().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(max) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);

        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settingsProportional = (SettingsProportional) speaker.getPitch().getSettings();
            settingsProportional.setOutputMax(max);
        } else if (speaker.getPitch().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settingsConstant = (SettingsConstant) speaker.getPitch().getSettings();
            settingsConstant.setValue(max);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.onMaxPitchChosen: unimplemented relationship");
        }
    }


    @Override
    public void onMinPitchChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");

        View view = dialogView.findViewById(R.id.linear_set_min_pitch);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentTextViewDescrp.setText(getString(speaker.getPitch().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(min) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);

        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settingsProportional = (SettingsProportional) speaker.getPitch().getSettings();
            settingsProportional.setOutputMax(min);
        } else if (speaker.getPitch().getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settingsConstant = (SettingsConstant) speaker.getPitch().getSettings();
            settingsConstant.setValue(min);
        } else {
            Log.e(Constants.LOG_TAG,"SpeakerDialog.onMinPitchChosen: unimplemented relationship");
        }
    }


    public interface DialogSpeakerListener {
        public void onSpeakerLinkListener(ArrayList<MelodySmartMessage> msgs);
    }

}
