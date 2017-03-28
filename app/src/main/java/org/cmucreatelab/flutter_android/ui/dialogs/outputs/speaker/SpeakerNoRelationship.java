package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerStateHelper implementation with No relationship assigned
 *
 */
public class SpeakerNoRelationship extends SpeakerStateHelper {


    public SpeakerNoRelationship(Speaker speaker) {
        super(TabType.VOLUME, speaker);
    }


    public static SpeakerNoRelationship newInstance(Speaker speaker) {
        return new SpeakerNoRelationship(speaker);
    }


    @Override
    public void updateView(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.updateView: no relationship; default to resource.");
    }


    @Override
    public void clickAdvancedSettings(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.clickAdvancedSettings: no relationship; default to resource.");
    }

    @Override
    public void clickMin(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.clickMin: no relationship; default to resource.");
    }


    @Override
    public void clickMax(SpeakerDialog dialog) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.clickMax: no relationship; default to resource.");
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimum(int minimum) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.setMinimum: attribute not implemented");
    }


    @Override
    public void setMaximum(int maximum) {
        Log.w(Constants.LOG_TAG, "SpeakerNoRelationship.setMaximum: attribute not implemented");
    }

}
