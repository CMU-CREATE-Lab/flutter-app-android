package org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPositionDialog;

/**
 * Created by mike on 4/13/17.
 *
 * ServoDialogStateHelper implementation with Frequency relationship.
 *
 */
public class ServoDialogFrequency extends ServoDialogStateHelper {


    ServoDialogFrequency(Servo servo) {
        super(servo);
    }


    public static ServoDialogStateHelper newInstance(Servo servo) {
        return new ServoDialogFrequency(servo);
    }


    @Override
    public void updateView(ServoDialog dialog) {
        Log.v(Constants.LOG_TAG,"ServoDialogFrequency.updateView");
        SettingsFrequency settings = (SettingsFrequency) getServo().getSettings();
        Sensor sensor = settings.getSensor();

        if (getServo().getSettings().getRelationship().getClass() != Frequency.class) {
            Log.e(Constants.LOG_TAG,"tried to run ServoDialog.updateViews on unimplemented relationship.");
        }

        // advanced settings
        dialog.advancedSettingsView.setVisibility(View.VISIBLE);

        // sensor
        dialog.linkedSensor.setVisibility(View.VISIBLE);

        // max
        ImageView maxPosImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_pos);
        RotateAnimation rotateAnimation = new RotateAnimation(0, settings.getOutputMax(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        maxPosImg.startAnimation(rotateAnimation);
        TextView maxPosTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_pos);
        TextView maxPosValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_pos_value);
        maxPosTxt.setText(sensor.getHighTextId());
        maxPosValue.setText(String.valueOf(settings.getOutputMax()));

        // min
        dialog.minPosLayout.setVisibility(View.VISIBLE);
        ImageView minPosImg = (ImageView) dialog.dialogView.findViewById(R.id.image_min_pos);
        rotateAnimation = new RotateAnimation(0, settings.getOutputMin(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        minPosImg.startAnimation(rotateAnimation);
        TextView minPosTxt = (TextView) dialog.dialogView.findViewById(R.id.text_min_pos);
        TextView minPosValue = (TextView) dialog.dialogView.findViewById(R.id.text_min_pos_value);
        minPosTxt.setText(sensor.getLowTextId());
        minPosValue.setText(String.valueOf(settings.getOutputMin()));
    }


    @Override
    public void clickMin(ServoDialog servoDialog) {
        DialogFragment dialog = MinPositionDialog.newInstance(Integer.valueOf(((SettingsFrequency)getServo().getSettings()).getOutputMin()), servoDialog);
        dialog.show(servoDialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMax(ServoDialog servoDialog) {
        DialogFragment dialog = MaxPositionDialog.newInstance(Integer.valueOf(((SettingsFrequency)getServo().getSettings()).getOutputMax()), servoDialog);
        dialog.show(servoDialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        ((SettingsFrequency)getServo().getSettings()).setAdvancedSettings(advancedSettings);
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        ((SettingsFrequency)getServo().getSettings()).setSensorPortNumber(sensor.getPortNumber());
    }


    @Override
    public void setMinimumPosition(int minimumPosition, TextView description, TextView item) {
        SettingsFrequency settings = (SettingsFrequency) getServo().getSettings();
        description.setText(settings.getSensor().getLowTextId());
        item.setText(String.valueOf(minimumPosition) + (char) 0x00B0);
        settings.setOutputMin(minimumPosition);
    }


    @Override
    public void setMaximumPosition(int maximumPosition, TextView description, TextView item) {
        Log.w(Constants.LOG_TAG,"ServoDialogFrequency.setMaximumPosition: attribute not implemented");
        SettingsFrequency settings = (SettingsFrequency) getServo().getSettings();
        description.setText(settings.getSensor().getHighTextId());
        item.setText(String.valueOf(maximumPosition) + (char) 0x00B0);
        settings.setOutputMax(maximumPosition);
    }

}
