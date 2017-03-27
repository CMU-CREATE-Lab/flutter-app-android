package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.DialogStateHelper;

/**
 * Created by mike on 3/24/17.
 *
 * DialogStateHelper for the Speaker dialog
 *
 */
public abstract class SpeakerStateHelper implements DialogStateHelper<SpeakerDialog> {

    private Speaker speaker;
    private TabType currentTab;


    public enum TabType {
        VOLUME, PITCH
    }


    public TabType getCurrentTab() {
        return currentTab;
    }


    public Speaker getSpeaker() {
        return speaker;
    }


    SpeakerStateHelper(TabType currentTab, Speaker speaker) {
        this.speaker = speaker;
        this.currentTab = currentTab;
    }


    public static SpeakerStateHelper newInstance(TabType tabType, Speaker speaker) {
        SpeakerStateHelper result;

        if (tabType == TabType.VOLUME) {
            result = SpeakerVolumeStateHelper.newInstance(speaker);
        } else if (tabType == TabType.PITCH) {
            result = SpeakerPitchStateHelper.newInstance(speaker);
        } else {
            Log.w(Constants.LOG_TAG, "SpeakerStateHelper.newInstance: Could not determine TabType, returning ");
            result = SpeakerNoRelationship.newInstance(speaker);
        }

        return result;
    }


    // click actions


    public abstract void clickMin();

    public abstract void clickMax();


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimum();

    public abstract void setMaximum();


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
