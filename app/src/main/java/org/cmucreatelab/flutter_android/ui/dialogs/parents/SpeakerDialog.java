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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseOutputDialog;
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
    private boolean isVolume;

    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;
    private Button buttonVolume;
    private Button buttonPitch;
    private RelativeLayout relativeVolume;
    private RelativeLayout relativePitch;

    private Button saveButton;
    private Speaker speaker;


    private void updatePitchViews(View view) {
        updateViews(view, speaker.getPitch());

        LinearLayout linkedSensor,minPitchLayout;
        ImageView advancedSettingsView = (ImageView) view.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) view.findViewById(R.id.linear_set_linked_sensor);
        minPitchLayout = (LinearLayout) view.findViewById(R.id.linear_set_min_pitch);

        // Pitch
        if (speaker.getPitch().getSettings().getRelationship().getClass() == Constant.class) {
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
            maxPitchValue.setText(String.valueOf(speaker.getPitch().getSettings().getOutputMax()) + " " + getString(R.string.hz));

            // min Pitch
            minPitchLayout.setVisibility(View.GONE);
        } else {
            if (speaker.getPitch().getSettings().getRelationship().getClass() != Proportional.class) {
                Log.e(Constants.LOG_TAG,"tried to run SpeakerDialog.updateViews on unimplemented relationship (pitch).");
            }
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
            maxPitchValue.setText(String.valueOf(speaker.getPitch().getSettings().getOutputMax()) + " " + getString(R.string.hz));

            // min Pitch
            minPitchLayout.setVisibility(View.VISIBLE);
            ImageView minPitch = (ImageView) view.findViewById(R.id.image_min_pitch);
            minPitch.setImageResource(R.drawable.link_icon_pitch);
            TextView minPitchTxt = (TextView) view.findViewById(R.id.text_min_pitch);
            minPitchTxt.setText(getString(speaker.getPitch().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.pitch));
            TextView minPitchValue = (TextView) view.findViewById(R.id.text_min_pitch_value);
            minPitchValue.setText(String.valueOf(speaker.getPitch().getSettings().getOutputMin()) + " " + getString(R.string.hz));
        }
    }


    private void updateVolumeViews(View view) {
        updateViews(view, speaker.getVolume());

        LinearLayout linkedSensor,minVolumeLayout;
        ImageView advancedSettingsView = (ImageView) view.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) view.findViewById(R.id.linear_set_linked_sensor);
        minVolumeLayout = (LinearLayout) view.findViewById(R.id.linear_set_min_volume);

        // Volume
        if (speaker.getVolume().getSettings().getRelationship().getClass() == Constant.class) {
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
            maxVolumeValue.setText(String.valueOf(speaker.getVolume().getSettings().getOutputMax()));

            // min Volume
            minVolumeLayout.setVisibility(View.GONE);
        } else {
            if (speaker.getVolume().getSettings().getRelationship().getClass() != Proportional.class) {
                Log.e(Constants.LOG_TAG,"tried to run SpeakerDialog.updateViews on unimplemented relationship (volume).");
            }
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
            maxVolumeValue.setText(String.valueOf(speaker.getVolume().getSettings().getOutputMax()));

            // min Volume
            minVolumeLayout.setVisibility(View.VISIBLE);
            ImageView minVolumeImg = (ImageView) view.findViewById(R.id.image_min_volume);
            minVolumeImg.setImageResource(R.drawable.link_icon_volume_low);
            TextView minVolumeTxt = (TextView) view.findViewById(R.id.text_min_volume);
            minVolumeTxt.setText(getString(speaker.getVolume().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.volume));
            TextView minVolumeValue = (TextView) view.findViewById(R.id.text_min_volume_value);
            minVolumeValue.setText(String.valueOf(speaker.getVolume().getSettings().getOutputMin()));
        }
    }


    private void updateViews(View view) {
        if (isVolume) {
            updateVolumeViews(view);
        } else {
            updatePitchViews(view);
        }

        saveButton.setEnabled(true);
        if (speaker.getVolume().getSettings().getRelationship().getClass() != Constant.class && speaker.getVolume().getSettings().getSensor().getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
            saveButton.setEnabled(false);
        }
        if (speaker.getPitch().getSettings().getRelationship().getClass() != Constant.class && speaker.getPitch().getSettings().getSensor().getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
            saveButton.setEnabled(false);
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


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);
        isVolume = true;

        // clone old object
        speaker = Speaker.newInstance((Speaker) getArguments().getSerializable(Speaker.SPEAKER_KEY));

        dialogSpeakerListener = (DialogSpeakerListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_SPEAKER);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view= inflater.inflate(R.layout.dialog_speakers, null);
        this.dialogView = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
        ButterKnife.bind(this, view);

        buttonVolume = (Button) view.findViewById(R.id.button_volume);
        buttonPitch = (Button) view.findViewById(R.id.button_pitch);
        relativeVolume = (RelativeLayout) view.findViewById(R.id.relative_volume);
        relativePitch = (RelativeLayout) view.findViewById(R.id.relative_pitch);
        saveButton = (Button) view.findViewById(R.id.button_save_link);

        updateViews(view);

        // set attributes so we can manipulate in the click events
        // TODO @tasota lots of these attributes need to be refactored
        ViewGroup view2 = (ViewGroup) view.findViewById(R.id.linear_set_linked_sensor);
        currentImageView = (ImageView) ((ViewGroup) view2).getChildAt(0);
        View layout = ((ViewGroup) view2).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        return builder.create();
    }


    // onClick Listeners


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, speaker);
        dialog.show(this.getFragmentManager(), "tag");
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
        speaker.getVolume().getSettings().setOutputMax(speaker.getVolume().getMax());
        speaker.getVolume().getSettings().setOutputMin(speaker.getVolume().getMin());
        speaker.getPitch().getSettings().setOutputMax(speaker.getPitch().getMax());
        speaker.getPitch().getSettings().setOutputMin(speaker.getPitch().getMin());

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().setSpeaker(speaker);

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
            updateViews(dialogView);
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
            updateViews(dialogView);
        }
    }


    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = SensorOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = RelationshipOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_volume)
    public void onClickSetMaxVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxVolume");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MaxVolumeDialog.newInstance(speaker.getVolume().getSettings().getOutputMax(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_volume)
    public void onClickSetMinVolume(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinVolume");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MinVolumeDialog.newInstance(speaker.getVolume().getSettings().getOutputMin(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_pitch)
    public void onClickSetMaxPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaxPitch");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MaxPitchDialog.newInstance(speaker.getPitch().getSettings().getOutputMax(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_pitch)
    public void onClickSetMinPitch(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPitch");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MinPitchDialog.newInstance(speaker.getPitch().getSettings().getOutputMin(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    // Option Listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        if (isVolume) {
            speaker.getVolume().getSettings().setAdvancedSettings(advancedSettings);
        } else {
            speaker.getPitch().getSettings().setAdvancedSettings(advancedSettings);
        }
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());
            // set sensor for the proper tab
            if (isVolume) {
                speaker.getVolume().getSettings().setSensor(sensor);
            } else {
                speaker.getPitch().getSettings().setSensor(sensor);
            }
            // set a sensor by default
            if (speaker.getVolume().getSettings().getRelationship().getClass() != Constant.class && speaker.getVolume().getSettings().getSensor().getClass() == NoSensor.class) {
                speaker.getVolume().getSettings().setRelationship(new Constant());
                speaker.getVolume().getSettings().setOutputMin(100);
                speaker.getVolume().getSettings().setOutputMax(100);
            }
            if (speaker.getPitch().getSettings().getRelationship().getClass() != Constant.class && speaker.getPitch().getSettings().getSensor().getClass() == NoSensor.class) {
                speaker.getPitch().getSettings().setRelationship(new Constant());
                speaker.getPitch().getSettings().setOutputMax(440);
                speaker.getPitch().getSettings().setOutputMin(440);
            }
        }
        updateViews(dialogView);
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());
        if (isVolume) {
            speaker.getVolume().getSettings().setRelationship(relationship);
            // set a sensor by default
            if (speaker.getPitch().getSettings().getRelationship().getClass() != Constant.class && speaker.getPitch().getSettings().getSensor().getClass() == NoSensor.class) {
                speaker.getPitch().getSettings().setRelationship(new Constant());
                speaker.getPitch().getSettings().setOutputMax(440);
                speaker.getPitch().getSettings().setOutputMin(440);
            }
        } else {
            speaker.getPitch().getSettings().setRelationship(relationship);
            // set a sensor by default
            if (speaker.getVolume().getSettings().getRelationship().getClass() != Constant.class && speaker.getVolume().getSettings().getSensor().getClass() == NoSensor.class) {
                speaker.getVolume().getSettings().setRelationship(new Constant());
                speaker.getVolume().getSettings().setOutputMin(100);
                speaker.getVolume().getSettings().setOutputMax(100);
            }
        }
        updateViews(dialogView);
    }


    @Override
    public void onMaxVolumeChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxVolumeChosen");
        currentTextViewDescrp.setText(getString(speaker.getVolume().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(max));
        currentImageView.setImageResource(R.drawable.link_icon_volume_high);
        speaker.getVolume().getSettings().setOutputMax(max);
    }


    @Override
    public void onMinVolumeChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(speaker.getVolume().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.volume));
        currentTextViewItem.setText(String.valueOf(min));
        currentImageView.setImageResource(R.drawable.link_icon_volume_low);
        speaker.getVolume().getSettings().setOutputMin(min);
    }


    @Override
    public void onMaxPitchChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(speaker.getPitch().getSettings().getSensor().getHighTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(max) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        speaker.getPitch().getSettings().setOutputMax(max);
    }


    @Override
    public void onMinPitchChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinVolumeChosen");
        currentTextViewDescrp.setText(getString(speaker.getPitch().getSettings().getSensor().getLowTextId()) + " " + getString(R.string.pitch));
        currentTextViewItem.setText(String.valueOf(min) + " " + getString(R.string.hz));
        currentImageView.setImageResource(R.drawable.link_icon_pitch);
        speaker.getPitch().getSettings().setOutputMin(min);
    }


    public interface DialogSpeakerListener {
        public void onSpeakerLinkListener(ArrayList<MelodySmartMessage> msgs);
    }

}
