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
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPositionDialog;

/**
 * Created by mike on 3/21/17.
 *
 * ServoDialogStateHelper implementation with Constant relationship
 *
 */
public class ServoDialogConstant extends ServoDialogStateHelper {


    ServoDialogConstant(Servo servo) {
        super(servo);
    }


    public static ServoDialogStateHelper newInstance(Servo servo) {
        return new ServoDialogConstant(servo);
    }


    @Override
    public void updateView(ServoDialog dialog) {
        Log.v(Constants.LOG_TAG,"ServoDialogConstant.updateView");
        SettingsConstant settings = (SettingsConstant) getServo().getSettings();

        // advanced settings
        dialog.advancedSettingsView.setVisibility(View.GONE);

        // sensor
        dialog.linkedSensor.setVisibility(View.GONE);

        // max
        ImageView maxPosImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_pos);
        RotateAnimation rotateAnimation = new RotateAnimation(0, settings.getValue(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        maxPosImg.startAnimation(rotateAnimation);
        TextView maxPosTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_pos);
        TextView maxPosValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_pos_value);
        maxPosTxt.setText("Value");
        maxPosValue.setText(String.valueOf(settings.getValue()));

        // min
        dialog.minPosLayout.setVisibility(View.GONE);
    }


    @Override
    public void clickMin(ServoDialog servoDialog) {
        DialogFragment dialog = MinPositionDialog.newInstance(Integer.valueOf(((SettingsConstant)getServo().getSettings()).getValue()), servoDialog);
        dialog.show(servoDialog.getFragmentManager(), "tag");
    }


    @Override
    public void clickMax(ServoDialog servoDialog) {
        DialogFragment dialog = MaxPositionDialog.newInstance(Integer.valueOf(((SettingsConstant)getServo().getSettings()).getValue()), servoDialog);
        dialog.show(servoDialog.getFragmentManager(), "tag");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG,"ServoDialogConstant.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG,"ServoDialogConstant.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimumPosition(int minimumPosition, TextView description, TextView item) {
        SettingsConstant settings = (SettingsConstant) getServo().getSettings();
        description.setText(settings.getSensor().getLowTextId());
        item.setText(String.valueOf(minimumPosition) + (char) 0x00B0);
        settings.setValue(minimumPosition);
    }


    @Override
    public void setMaximumPosition(int maximumPosition, TextView description, TextView item) {
        SettingsConstant settings = (SettingsConstant) getServo().getSettings();
        description.setText(settings.getSensor().getHighTextId());
        item.setText(String.valueOf(maximumPosition) + (char) 0x00B0);
        settings.setValue(maximumPosition);
    }

}
