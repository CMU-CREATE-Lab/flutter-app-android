package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_pitch;

import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerPitchStateHelper implementation with Proportional relationship
 *
 */
public class SpeakerPitchProportional extends SpeakerPitchStateHelper {


    SpeakerPitchProportional(Speaker speaker) {
        super(speaker);
    }


    public static SpeakerPitchProportional newInstance(Speaker speaker) {
        return new SpeakerPitchProportional(speaker);
    }


    @Override
    public void updateView(SpeakerDialog dialog) {
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        LinearLayout linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        LinearLayout minPitchLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_pitch);
        LinearLayout minVolumeLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_volume);

        SettingsProportional pitchSettings = (SettingsProportional) getSpeaker().getPitch().getSettings();
        // advanced settings
        advancedSettingsView.setVisibility(View.VISIBLE);

        // sensor
        linkedSensor.setVisibility(View.VISIBLE);

        ImageView currentImageView = (ImageView) linkedSensor.findViewById(R.id.image_sensor);
        ImageView currentImageViewHighlight = (ImageView) linkedSensor.findViewById(R.id.image_sensor_highlight);
        TextView currentTextViewDescrp = (TextView) linkedSensor.findViewById(R.id.text_sensor_link);
        TextView currentTextViewItem = (TextView) linkedSensor.findViewById(R.id.text_sensor_type);

        currentImageView.setImageResource(getSpeaker().getPitch().getSettings().getSensor().getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(getSpeaker().getPitch().getSettings().getSensor().getSensorTypeId());

        // max Pitch
        ImageView maxPitch = (ImageView) dialog.dialogView.findViewById(R.id.image_max_pitch);
        maxPitch.setImageResource(R.drawable.link_icon_pitch);
        TextView maxPitchTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_pitch);
        maxPitchTxt.setText(dialog.getString(getSpeaker().getPitch().getSettings().getSensor().getHighTextId()) + " " + dialog.getString(R.string.pitch));
        TextView maxPitchValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_pitch_value);
        maxPitchValue.setText(String.valueOf(pitchSettings.getOutputMax()) + " " + dialog.getString(R.string.hz));

        // min Pitch
        minPitchLayout.setVisibility(View.VISIBLE);
        ImageView minPitch = (ImageView) dialog.dialogView.findViewById(R.id.image_min_pitch);
        minPitch.setImageResource(R.drawable.link_icon_pitch);
        TextView minPitchTxt = (TextView) dialog.dialogView.findViewById(R.id.text_min_pitch);
        minPitchTxt.setText(dialog.getString(getSpeaker().getPitch().getSettings().getSensor().getLowTextId()) + " " + dialog.getString(R.string.pitch));
        TextView minPitchValue = (TextView) dialog.dialogView.findViewById(R.id.text_min_pitch_value);
        minPitchValue.setText(String.valueOf(pitchSettings.getOutputMin()) + " " + dialog.getString(R.string.hz));
    }


    @Override
    public void clickAdvancedSettings(SpeakerDialog dialog) {
        DialogFragment fragment = AdvancedSettingsDialog.newInstance(dialog, getSpeaker().getPitch());
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMin(SpeakerDialog dialog) {
        SettingsProportional pitchSettings = (SettingsProportional) getSpeaker().getPitch().getSettings();
        DialogFragment fragment = MinPitchDialog.newInstance(pitchSettings.getOutputMin(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMax(SpeakerDialog dialog) {
        SettingsProportional pitchSettings = (SettingsProportional) getSpeaker().getPitch().getSettings();
        DialogFragment fragment = MaxPitchDialog.newInstance(pitchSettings.getOutputMax(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        SettingsProportional settingsPitch = (SettingsProportional) getSpeaker().getPitch().getSettings();
        settingsPitch.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getPitch().getSettings();
        settingsProportional.setSensorPortNumber(sensor.getPortNumber());
    }


    @Override
    public void setMinimum(int minimum) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getPitch().getSettings();
        settingsProportional.setOutputMax(minimum);
    }


    @Override
    public void setMaximum(int maximum) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getPitch().getSettings();
        settingsProportional.setOutputMax(maximum);
    }

}
