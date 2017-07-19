package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MaxPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MaxVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MinPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MinVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.SensorOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol.InputTypes.NOT_SET;

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

    public View dialogView;

    private SpeakerStateHelper stateHelper;
    private DialogSpeakerListener dialogSpeakerListener;
    private Speaker speaker;

    // animations
    private AlphaAnimation blinkAnimation;

    private void updateViews() {
        switch (stateHelper.getCurrentTab()) {
            case VOLUME:
                super.updateViews(dialogView,speaker.getVolume());
                break;
            case PITCH:
                super.updateViews(dialogView,speaker.getPitch());
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType.");
        }

        stateHelper.updateView(this);

        if (speaker.getVolume().getSettings().getSensor().getSensorType() == NOT_SET && speaker.getPitch().getSettings().getSensor().getSensorType() == NOT_SET) {
            ImageView currentImageViewHighlight = (ImageView) dialogView.findViewById(R.id.image_sensor_highlight);
            currentImageViewHighlight.startAnimation(blinkAnimation);
        }

        Button saveButton = (Button) dialogView.findViewById(R.id.button_save_link);
        Button removeButton = (Button) dialogView.findViewById(R.id.button_remove_link);
        saveButton.setEnabled(stateHelper.canSaveLink());
        removeButton.setEnabled(stateHelper.canRemoveLink());
    }


    public static SpeakerDialog newInstance(Speaker speaker, Serializable activity) {
        SpeakerDialog ledDialog = new SpeakerDialog();

        Bundle args = new Bundle();
        args.putSerializable(Speaker.SPEAKER_KEY, speaker);
        args.putSerializable(Constants.SerializableKeys.DIALOG_SPEAKER, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        // clone old object
        speaker = Speaker.newInstance((Speaker) getArguments().getSerializable(Speaker.SPEAKER_KEY));
        stateHelper = SpeakerStateHelper.newInstance(SpeakerStateHelper.TabType.VOLUME, speaker);
        dialogSpeakerListener = (DialogSpeakerListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_SPEAKER);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view= inflater.inflate(R.layout.dialog_speakers, null);
        this.dialogView = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
        ButterKnife.bind(this, view);

        // Create animation to highlight a sensor that's never been linked
        blinkAnimation = new AlphaAnimation((float)0.8, 0);
        blinkAnimation.setDuration(900);
        blinkAnimation.setStartOffset(150);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);

        updateViews();

        return builder.create();
    }


    // onClick Listeners


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        stateHelper.clickAdvancedSettings(this);
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
        RelativeLayout relativeVolume = (RelativeLayout) dialogView.findViewById(R.id.relative_volume);
        RelativeLayout relativePitch = (RelativeLayout) dialogView.findViewById(R.id.relative_pitch);

        if (stateHelper.getCurrentTab() != SpeakerStateHelper.TabType.VOLUME) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_left));
            buttonVolume.setTextColor(Color.WHITE);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_right));
            buttonPitch.setTextColor(Color.GRAY);
            relativePitch.setVisibility(View.GONE);
            relativeVolume.setVisibility(View.VISIBLE);
            stateHelper = SpeakerStateHelper.newInstance(SpeakerStateHelper.TabType.VOLUME, speaker);
            updateViews();
        }
    }


    @OnClick(R.id.button_pitch)
    public void onClickPitch() {
        Log.d(Constants.LOG_TAG, "onClickPitch");
        Button buttonVolume = (Button) dialogView.findViewById(R.id.button_volume);
        Button buttonPitch = (Button) dialogView.findViewById(R.id.button_pitch);
        RelativeLayout relativeVolume = (RelativeLayout) dialogView.findViewById(R.id.relative_volume);
        RelativeLayout relativePitch = (RelativeLayout) dialogView.findViewById(R.id.relative_pitch);

        if (stateHelper.getCurrentTab() != SpeakerStateHelper.TabType.PITCH) {
            buttonVolume.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_gray_white_left));
            buttonVolume.setTextColor(Color.GRAY);
            buttonPitch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_green_button_right));
            buttonPitch.setTextColor(Color.WHITE);
            relativePitch.setVisibility(View.VISIBLE);
            relativeVolume.setVisibility(View.GONE);
            stateHelper = SpeakerStateHelper.newInstance(SpeakerStateHelper.TabType.PITCH, speaker);
            updateViews();
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

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.VOLUME) {
            stateHelper.clickMax(this);
        } else {
            Log.e(Constants.LOG_TAG,"onClickSetMaxVolume but DialogStateHelper.currentTab is not on VOLUME");
        }
    }


    @OnClick(R.id.linear_set_min_volume)
    public void onClickSetMinVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinVolume");

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.VOLUME) {
            stateHelper.clickMin(this);
        } else {
            Log.e(Constants.LOG_TAG,"onClickSetMinVolume but DialogStateHelper.currentTab is not on VOLUME");
        }
    }


    @OnClick(R.id.linear_set_max_pitch)
    public void onClickSetMaxPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxPitch");

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.PITCH) {
            stateHelper.clickMax(this);
        } else {
            Log.e(Constants.LOG_TAG,"onClickSetMaxPitch but DialogStateHelper.currentTab is not on PITCH");
        }
    }


    @OnClick(R.id.linear_set_min_pitch)
    public void onClickSetMinPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPitch");

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.PITCH) {
            stateHelper.clickMin(this);
        } else {
            Log.e(Constants.LOG_TAG,"onClickSetMinPitch but DialogStateHelper.currentTab is not on PITCH");
        }
    }


    // Option Listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        stateHelper.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");

            ImageView currentImageView = (ImageView) dialogView.findViewById(R.id.image_sensor);
            ImageView currentImageViewHighlight = (ImageView) dialogView.findViewById(R.id.image_sensor_highlight);
            TextView currentTextViewDescrp = (TextView) dialogView.findViewById(R.id.text_sensor_link);
            TextView currentTextViewItem = (TextView) dialogView.findViewById(R.id.text_sensor_type);

            currentImageView.setImageResource(sensor.getGreenImageId());
            currentImageViewHighlight.clearAnimation();
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());

            // set sensor for the proper tab
            stateHelper.setLinkedSensor(sensor);

            // set a sensor by default
            if (!speaker.getVolume().getSettings().isSettable()) {
                SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getVolume().getSettings());
                settingsConstant.setValue(100);
                speaker.getVolume().setSettings(settingsConstant);
                stateHelper = SpeakerStateHelper.newInstance(stateHelper.getCurrentTab(), speaker);
            }
            if (!speaker.getPitch().getSettings().isSettable()) {
                SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getPitch().getSettings());
                settingsConstant.setValue(440);
                speaker.getPitch().setSettings(settingsConstant);
                stateHelper = SpeakerStateHelper.newInstance(stateHelper.getCurrentTab(), speaker);
            }
        }
        updateViews();
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

        switch (stateHelper.getCurrentTab()) {
            case VOLUME:
                speaker.getVolume().setSettings(Settings.newInstance(speaker.getVolume().getSettings(), relationship));
                // set a sensor by default
                if (!speaker.getPitch().getSettings().isSettable()) {
                    SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getPitch().getSettings());
                    settingsConstant.setValue(440);
                    speaker.getPitch().setSettings(settingsConstant);
                }
                stateHelper = SpeakerStateHelper.newInstance(SpeakerStateHelper.TabType.VOLUME, speaker);
                break;
            case PITCH:
                speaker.getPitch().setSettings(Settings.newInstance(speaker.getPitch().getSettings(), relationship));
                // set a sensor by default
                if (!speaker.getVolume().getSettings().isSettable()) {
                    SettingsConstant settingsConstant = SettingsConstant.newInstance(speaker.getVolume().getSettings());
                    settingsConstant.setValue(100);
                    speaker.getVolume().setSettings(settingsConstant);
                }
                stateHelper = SpeakerStateHelper.newInstance(SpeakerStateHelper.TabType.PITCH, speaker);
                break;
            default:
                Log.e(Constants.LOG_TAG,"Failed to parse TabType");
                stateHelper = SpeakerStateHelper.newInstance(null, speaker);
        }

        updateViews();
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

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.VOLUME) {
            stateHelper.setMaximum(max);
        } else {
            Log.e(Constants.LOG_TAG,"onMaxVolumeChosen but DialogStateHelper.currentTab is not on VOLUME");
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

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.VOLUME) {
            stateHelper.setMinimum(min);
        } else {
            Log.e(Constants.LOG_TAG,"onMinVolumeChosen but DialogStateHelper.currentTab is not on VOLUME");
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

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.PITCH) {
            stateHelper.setMaximum(max);
        } else {
            Log.e(Constants.LOG_TAG,"onMaxPitchChosen but DialogStateHelper.currentTab is not on VOLUME");
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

        if (stateHelper.getCurrentTab() == SpeakerStateHelper.TabType.PITCH) {
            stateHelper.setMinimum(min);
        } else {
            Log.e(Constants.LOG_TAG,"onMaxPitchChosen but DialogStateHelper.currentTab is not on VOLUME");
        }
    }


    public interface DialogSpeakerListener {
        public void onSpeakerLinkListener(ArrayList<MelodySmartMessage> msgs);
    }

}
