package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.DialogStateHelper;

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

        if (triColorLed.getRedLed().getSettings().getClass() == triColorLed.getGreenLed().getSettings().getClass() && triColorLed.getGreenLed().getSettings().getClass() == triColorLed.getBlueLed().getSettings().getClass()) {
            Relationship relationship = triColorLed.getRedLed().getSettings().getRelationship();
            if (relationship.getClass() == Proportional.class) {
                result = LedDialogProportional.newInstance(triColorLed);
            } else if (relationship.getClass() == Constant.class) {
                result = LedDialogConstant.newInstance(triColorLed);
            } else if (relationship.getClass() == Amplitude.class) {
                result = LedDialogAmplitude.newInstance(triColorLed);
            } else {
                Log.w(Constants.LOG_TAG,"LedDialogStateHelper.newInstance: relationship not implemented");
                result = LedDialogNoRelationship.newInstance(triColorLed);
            }
        } else {
            Log.e(Constants.LOG_TAG,"LedDialogStateHelper.newInstance: RGB Led Relationships do not match.");
            result = LedDialogNoRelationship.newInstance(triColorLed);
        }

        return result;
    }


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimumColor(int red, int green, int blue);

    public abstract void setMaximumColor(int red, int green, int blue);


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
