package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker;

import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;

/**
 * Created by mike on 3/27/17.
 */

public abstract class SpeakerPitchStateHelper extends SpeakerStateHelper {


    SpeakerPitchStateHelper(Speaker speaker) {
        super(TabType.PITCH, speaker);
    }


    public static SpeakerPitchStateHelper newInstance(Speaker speaker) {
        SpeakerPitchStateHelper result;

        result = new SpeakerPitchStateHelper(speaker) {
            @Override
            public void clickMin() {

            }

            @Override
            public void clickMax() {

            }

            @Override
            public void setAdvancedSettings(AdvancedSettings advancedSettings) {

            }

            @Override
            public void setLinkedSensor(Sensor sensor) {

            }

            @Override
            public void setMinimum() {

            }

            @Override
            public void setMaximum() {

            }

            @Override
            public void updateView(SpeakerDialog dialog) {

            }
        };

        return result;
    }

}
