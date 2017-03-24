package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.DialogStateHelper;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo.ServoDialogConstant;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo.ServoDialogNoRelationship;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo.ServoDialogProportional;

/**
 * Created by mike on 3/24/17.
 *
 * DialogStateHelper for the Led dialog
 *
 */
public abstract class LedDialogStateHelper implements DialogStateHelper<LedDialog> {

    private TriColorLed triColorLed;


    public TriColorLed getTriColorLed() {
        return triColorLed;
    }


    LedDialogStateHelper(TriColorLed triColorLed) {
        this.triColorLed = triColorLed;
    }


    public static LedDialogStateHelper newInstance(TriColorLed triColorLed) {
        LedDialogStateHelper result;

        result = null;
//        if (settings.getClass() == SettingsProportional.class) {
//            result = ServoDialogProportional.newInstance(servo);
//        } else if (settings.getClass() == SettingsConstant.class) {
//            result = ServoDialogConstant.newInstance(servo);
//        } else {
//            Log.w(Constants.LOG_TAG,"ServoDialogStateHelper.newInstance: unimplmeneted relationship, returning ServoDialogNoRelationship.");
//            result = ServoDialogNoRelationship.newInstance(servo);
//        }

        return result;
    }


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimumColor();

    public abstract void setMaximumColor();


    // DialogStateHelper implementation


    @Override
    public boolean canRemoveLink() {
        return triColorLed.getRedLed().isLinked() && triColorLed.getGreenLed().isLinked() && triColorLed.getBlueLed().isLinked();
    }


    @Override
    public boolean canSaveLink() {
        return triColorLed.getRedLed().getSettings().isSettable() && triColorLed.getGreenLed().getSettings().isSettable() && triColorLed.getBlueLed().getSettings().isSettable();
    }


    public abstract void updateView(LedDialog dialog);

}
