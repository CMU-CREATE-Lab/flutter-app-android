package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/24/17.
 *
 * LedDialogStateHelper implementation with No relationship assigned
 *
 */
public class LedDialogNoRelationship extends LedDialogStateHelper {


    LedDialogNoRelationship(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogNoRelationship newInstance(TriColorLed triColorLed) {
        return new LedDialogNoRelationship(triColorLed);
    }


    @Override
    public void updateView(LedDialog dialog) {
        Log.w(Constants.LOG_TAG,"LedDialogNoRelationship.updateView: no relationship; default to resource.");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG,"LedDialogNoRelationship.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG,"LedDialogNoRelationship.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimumColor(int red, int green, int blue) {
        Log.w(Constants.LOG_TAG,"LedDialogNoRelationship.setMinimumColor: attribute not implemented");
    }


    @Override
    public void setMaximumColor(int red, int green, int blue) {
        Log.w(Constants.LOG_TAG,"LedDialogNoRelationship.setMaximumColor: attribute not implemented");
    }

}
