package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.DialogStateHelper;

/**
 * Created by mike on 3/24/17.
 *
 * DialogStateHelper for the Speaker dialog
 *
 */
public abstract class SpeakerStateHelper implements DialogStateHelper<SpeakerDialog> {

    private Speaker speaker;


    public Speaker getSpeaker() {
        return speaker;
    }


    SpeakerStateHelper(Speaker speaker) {
        this.speaker = speaker;
    }


    public static SpeakerStateHelper newInstance(Speaker speaker) {
        SpeakerStateHelper result;

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


    // click actions


    public abstract void clickMinPitch();

    public abstract void clickMaxPitch();

    public abstract void clickMinVolume();

    public abstract void clickMaxVolume();


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimumPitch();

    public abstract void setMaximumPitch();

    public abstract void setMinimumVolume();

    public abstract void setMaximumVolume();


    // DialogStateHelper implementation


    @Override
    public boolean canRemoveLink() {
        return speaker.getPitch().isLinked() && speaker.getVolume().isLinked();
    }


    @Override
    public boolean canSaveLink() {
        return speaker.getPitch().getSettings().isSettable() && speaker.getVolume().getSettings().isSettable();
    }


    public abstract void updateView(SpeakerDialog dialog);

}
