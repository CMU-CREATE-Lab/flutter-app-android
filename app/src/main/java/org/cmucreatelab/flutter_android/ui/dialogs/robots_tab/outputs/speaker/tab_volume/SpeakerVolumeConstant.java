package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker.tab_volume;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MaxVolumeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker.SpeakerDialog;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerVolumeStateHelper implementation with Constant relationship
 *
 */
public class SpeakerVolumeConstant extends SpeakerVolumeStateHelper {


    SpeakerVolumeConstant(Speaker speaker) {
        super(speaker);
    }


    public static SpeakerVolumeConstant newInstance(Speaker speaker) {
        return new SpeakerVolumeConstant(speaker);
    }


    @Override
    public void updateView(SpeakerDialog dialog) {
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        LinearLayout linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        LinearLayout minVolumeLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_volume);

        SettingsConstant volumeSettings = (SettingsConstant) getSpeaker().getVolume().getSettings();

        // advanced settings
        advancedSettingsView.setVisibility(View.GONE);

        // sensor
        linkedSensor.setVisibility(View.GONE);

        // max Volume
        ImageView maxVolumeImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_volume);
        maxVolumeImg.setImageResource(R.drawable.link_icon_volume_high);
        TextView maxVolumeTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_volume);
        maxVolumeTxt.setText(dialog.getString(getSpeaker().getVolume().getSettings().getSensor().getHighTextId()) + " " + dialog.getString(R.string.volume));
        TextView maxVolumeValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_volume_value);
        maxVolumeValue.setText(String.valueOf(volumeSettings.getValue()));

        // min Volume
        minVolumeLayout.setVisibility(View.GONE);
    }


    @Override
    public void clickAdvancedSettings(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerVolumeConstant.clickAdvancedSettings: not implemented; default to resource.");
    }


    @Override
    public void clickMin(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerVolumeConstant.clickMin: not implemented; default to resource.");
    }


    @Override
    public void clickMax(SpeakerDialog dialog) {
        SettingsConstant volumeSettings = (SettingsConstant) getSpeaker().getVolume().getSettings();
        DialogFragment fragment = MaxVolumeDialog.newInstance(volumeSettings.getValue(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG, "SpeakerVolumeConstant.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG, "SpeakerVolumeConstant.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimum(int minimum) {
        Log.w(Constants.LOG_TAG, "SpeakerVolumeConstant.setMinimum: attribute not implemented");
    }


    @Override
    public void setMaximum(int maximum) {
        SettingsConstant settingsConstant = (SettingsConstant) getSpeaker().getVolume().getSettings();
        settingsConstant.setValue(maximum);
    }

}
