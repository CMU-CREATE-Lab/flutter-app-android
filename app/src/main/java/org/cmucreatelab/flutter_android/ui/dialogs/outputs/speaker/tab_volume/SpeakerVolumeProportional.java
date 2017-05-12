package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_volume;

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
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerVolumeStateHelper implementation with Proportional relationship
 *
 */
public class SpeakerVolumeProportional extends SpeakerVolumeStateHelper {


    SpeakerVolumeProportional(Speaker speaker) {
        super(speaker);
    }


    public static SpeakerVolumeProportional newInstance(Speaker speaker) {
        return new SpeakerVolumeProportional(speaker);
    }


    @Override
    public void updateView(SpeakerDialog dialog) {
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        LinearLayout linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        LinearLayout minVolumeLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_volume);

        SettingsProportional volumeSettings = (SettingsProportional) getSpeaker().getVolume().getSettings();

        // advanced settings
        advancedSettingsView.setVisibility(View.VISIBLE);

        // sensor
        linkedSensor.setVisibility(View.VISIBLE);

        ImageView currentImageView = (ImageView) linkedSensor.findViewById(R.id.image_sensor);
        ImageView currentImageViewHighlight = (ImageView) linkedSensor.findViewById(R.id.image_sensor_highlight);
        TextView currentTextViewDescrp = (TextView) linkedSensor.findViewById(R.id.text_sensor_link);
        TextView currentTextViewItem = (TextView) linkedSensor.findViewById(R.id.text_sensor_type);

        currentImageView.setImageResource(getSpeaker().getVolume().getSettings().getSensor().getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(getSpeaker().getVolume().getSettings().getSensor().getSensorTypeId());

        // max Volume
        ImageView maxVolumeImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_volume);
        maxVolumeImg.setImageResource(R.drawable.link_icon_volume_high);
        TextView maxVolumeTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_volume);
        maxVolumeTxt.setText(dialog.getString(getSpeaker().getVolume().getSettings().getSensor().getHighTextId()) + " " + dialog.getString(R.string.volume));
        TextView maxVolumeValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_volume_value);
        maxVolumeValue.setText(String.valueOf(volumeSettings.getOutputMax()));

        // min Volume
        minVolumeLayout.setVisibility(View.VISIBLE);
        ImageView minVolumeImg = (ImageView) dialog.dialogView.findViewById(R.id.image_min_volume);
        minVolumeImg.setImageResource(R.drawable.link_icon_volume_low);
        TextView minVolumeTxt = (TextView) dialog.dialogView.findViewById(R.id.text_min_volume);
        minVolumeTxt.setText(dialog.getString(getSpeaker().getVolume().getSettings().getSensor().getLowTextId()) + " " + dialog.getString(R.string.volume));
        TextView minVolumeValue = (TextView) dialog.dialogView.findViewById(R.id.text_min_volume_value);
        minVolumeValue.setText(String.valueOf(volumeSettings.getOutputMin()));
    }


    @Override
    public void clickAdvancedSettings(SpeakerDialog dialog) {
        DialogFragment fragment = AdvancedSettingsDialog.newInstance(dialog, getSpeaker().getVolume());
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMin(SpeakerDialog dialog) {
        SettingsProportional volumeSettings = (SettingsProportional) getSpeaker().getVolume().getSettings();
        DialogFragment fragment = MinVolumeDialog.newInstance(volumeSettings.getOutputMin(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMax(SpeakerDialog dialog) {
        SettingsProportional volumeSettings = (SettingsProportional) getSpeaker().getVolume().getSettings();
        DialogFragment fragment = MaxVolumeDialog.newInstance(volumeSettings.getOutputMax(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        SettingsProportional settingsVolume = (SettingsProportional) getSpeaker().getVolume().getSettings();
        settingsVolume.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getVolume().getSettings();
        settingsProportional.setSensorPortNumber(sensor.getPortNumber());
    }


    @Override
    public void setMinimum(int minimum) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getVolume().getSettings();
        settingsProportional.setOutputMin(minimum);
    }


    @Override
    public void setMaximum(int maximum) {
        SettingsProportional settingsProportional = (SettingsProportional) getSpeaker().getVolume().getSettings();
        settingsProportional.setOutputMax(maximum);
    }

}
