package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_pitch;

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
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPitchDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerPitchStateHelper implementation with Constant relationship
 *
 */
public class SpeakerPitchConstant extends SpeakerPitchStateHelper {


    SpeakerPitchConstant(Speaker speaker) {
        super(speaker);
    }


    public static SpeakerPitchConstant newInstance(Speaker speaker) {
        return new SpeakerPitchConstant(speaker);
    }


    @Override
    public void updateView(SpeakerDialog dialog) {
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        LinearLayout linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        LinearLayout minPitchLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_pitch);
        LinearLayout minVolumeLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_volume);

        SettingsConstant pitchSettings = (SettingsConstant) getSpeaker().getPitch().getSettings();

        // advanced settings
        advancedSettingsView.setVisibility(View.GONE);

        // sensor
        linkedSensor.setVisibility(View.GONE);

        // max Pitch
        ImageView maxPitch = (ImageView) dialog.dialogView.findViewById(R.id.image_max_pitch);
        maxPitch.setImageResource(R.drawable.link_icon_pitch);
        TextView maxPitchTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_pitch);
        maxPitchTxt.setText(dialog.getString(getSpeaker().getPitch().getSettings().getSensor().getHighTextId()) + " " + dialog.getString(R.string.pitch));
        TextView maxPitchValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_pitch_value);
        maxPitchValue.setText(String.valueOf(pitchSettings.getValue()) + " " + dialog.getString(R.string.hz));

        // min Pitch
        minPitchLayout.setVisibility(View.GONE);
    }


    @Override
    public void clickAdvancedSettings(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerPitchConstant.clickAdvancedSettings: not implemented; default to resource.");
    }


    @Override
    public void clickMin(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerPitchConstant.clickMin: not implemented; default to resource.");
    }


    @Override
    public void clickMax(SpeakerDialog dialog) {
        SettingsConstant pitchSettings = (SettingsConstant) getSpeaker().getPitch().getSettings();
        DialogFragment fragment = MaxPitchDialog.newInstance(pitchSettings.getValue(),dialog);
        fragment.show(dialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG, "SpeakerPitchConstant.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG, "SpeakerPitchConstant.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimum(int minimum) {
        Log.w(Constants.LOG_TAG, "SpeakerPitchConstant.setMinimum: attribute not implemented");
    }


    @Override
    public void setMaximum(int maximum) {
        SettingsConstant settingsConstant = (SettingsConstant) getSpeaker().getPitch().getSettings();
        settingsConstant.setValue(maximum);
    }

}
