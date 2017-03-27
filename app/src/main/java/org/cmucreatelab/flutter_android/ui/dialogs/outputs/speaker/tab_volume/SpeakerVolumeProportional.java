package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_volume;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;

/**
 * Created by mike on 3/27/17.
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
        LinearLayout minPitchLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_pitch);
        LinearLayout minVolumeLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_volume);

        SettingsProportional volumeSettings = (SettingsProportional) getSpeaker().getVolume().getSettings();

        // advanced settings
        advancedSettingsView.setVisibility(View.VISIBLE);

        // sensor
        linkedSensor.setVisibility(View.VISIBLE);
        ((ImageView)linkedSensor.getChildAt(0)).setImageResource(getSpeaker().getVolume().getSettings().getSensor().getGreenImageId());
        ViewGroup sensorViewGroup = ((ViewGroup)linkedSensor.getChildAt(1));
        ((TextView)sensorViewGroup.getChildAt(0)).setText(R.string.linked_sensor);
        ((TextView)sensorViewGroup.getChildAt(1)).setText(getSpeaker().getVolume().getSettings().getSensor().getSensorTypeId());

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
    public void clickMin() {

    }


    @Override
    public void clickMax() {

    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        SettingsProportional settingsVolume = (SettingsProportional) getSpeaker().getVolume().getSettings();
        settingsVolume.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {

    }


    @Override
    public void setMinimum(int minimum) {

    }


    @Override
    public void setMaximum(int maximum) {

    }

}
