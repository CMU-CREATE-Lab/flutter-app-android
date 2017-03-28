package org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo;

import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.DialogStateHelper;

/**
 * Created by mike on 3/21/17.
 *
 * DialogStateHelper for the Servo dialog
 *
 */
public abstract class ServoDialogStateHelper implements DialogStateHelper<ServoDialog> {

    private Servo servo;


    public Servo getServo() {
        return servo;
    }


    ServoDialogStateHelper(Servo servo) {
        this.servo = servo;
    }


    public static ServoDialogStateHelper newInstance(Servo servo) {
        ServoDialogStateHelper result;
        Settings settings = servo.getSettings();

        if (settings.getClass() == SettingsProportional.class) {
            result = ServoDialogProportional.newInstance(servo);
        } else if (settings.getClass() == SettingsConstant.class) {
            result = ServoDialogConstant.newInstance(servo);
        } else {
            Log.w(Constants.LOG_TAG,"ServoDialogStateHelper.newInstance: unimplmeneted relationship, returning ServoDialogNoRelationship.");
            result = ServoDialogNoRelationship.newInstance(servo);
        }

        return result;
    }


    // click actions


    public abstract void clickMin(ServoDialog servoDialog);

    public abstract void clickMax(ServoDialog servoDialog);


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimumPosition(int minimumPosition, TextView description, TextView item);

    public abstract void setMaximumPosition(int maximumPosition, TextView description, TextView item);


    // DialogStateHelper implementation


    @Override
    public boolean canRemoveLink() {
        return servo.isLinked();
    }


    @Override
    public boolean canSaveLink() {
        return servo.getSettings().isSettable();
    }


    public abstract void updateView(ServoDialog dialog);

}
