package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.DialogStateHelper;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker.tab_pitch.SpeakerPitchStateHelper;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.speaker.tab_volume.SpeakerVolumeStateHelper;

/**
 * Created by mike on 3/24/17.
 *
 * DialogStateHelper for the Speaker dialog; tracks the Tab state as well as Relationship type for selected tab
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


    protected SpeakerStateHelper(TabType currentTab, Speaker speaker) {
        this.speaker = speaker;
        this.currentTab = currentTab;
    }


    public static SpeakerStateHelper newInstance(TabType tabType, Speaker speaker) {
        SpeakerStateHelper result = null;

        if (tabType == TabType.VOLUME) {
            result = SpeakerVolumeStateHelper.newInstance(speaker);
        } else if (tabType == TabType.PITCH) {
            result = SpeakerPitchStateHelper.newInstance(speaker);
        } else {
            Log.w(Constants.LOG_TAG, "SpeakerStateHelper.newInstance: Could not determine TabType, returning ");
        }
        if (result == null) {
            result = SpeakerNoRelationship.newInstance(speaker);
        }

        return result;
    }


    // click actions


    public abstract void clickAdvancedSettings(SpeakerDialog dialog);

    public abstract void clickMin(SpeakerDialog dialog);

    public abstract void clickMax(SpeakerDialog dialog);


    // set actions


    public abstract void setAdvancedSettings(AdvancedSettings advancedSettings);

    public abstract void setLinkedSensor(Sensor sensor);

    public abstract void setMinimum(int minimum);

    public abstract void setMaximum(int maximum);


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
