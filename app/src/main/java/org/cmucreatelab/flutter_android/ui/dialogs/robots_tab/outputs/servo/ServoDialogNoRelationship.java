package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo;

import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/21/17.
 *
 * ServoDialogStateHelper implementation with No relationship assigned
 *
 */
public class ServoDialogNoRelationship extends ServoDialogStateHelper {


    public ServoDialogNoRelationship(Servo servo) {
        super(servo);
    }


    public static ServoDialogStateHelper newInstance(Servo servo) {
        return new ServoDialogProportional(servo);
    }


    @Override
    public void updateView(ServoDialog dialog) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.updateView: no relationship; default to resource.");
    }


    @Override
    public void clickMin(ServoDialog servoDialog) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.clickMin: no relationship; default to resource.");
    }


    @Override
    public void clickMax(ServoDialog servoDialog) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.clickMax: no relationship; default to resource.");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimumPosition(int minimumPosition, TextView description, TextView item) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.setMinimumPosition: attribute not implemented");
    }


    @Override
    public void setMaximumPosition(int maximumPosition, TextView description, TextView item) {
        Log.w(Constants.LOG_TAG,"ServoDialogNoRelationship.setMaximumPosition: attribute not implemented");
    }

}
