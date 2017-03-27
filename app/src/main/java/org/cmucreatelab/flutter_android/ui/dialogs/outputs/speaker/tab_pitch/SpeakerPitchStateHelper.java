package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_pitch;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerStateHelper;

/**
 * Created by mike on 3/27/17.
 */

public abstract class SpeakerPitchStateHelper extends SpeakerStateHelper {


    SpeakerPitchStateHelper(Speaker speaker) {
        super(TabType.PITCH, speaker);
    }


    public static SpeakerPitchStateHelper newInstance(Speaker speaker) {
        SpeakerPitchStateHelper result;

        if (speaker.getPitch().getSettings().getClass() == SettingsProportional.class) {
            result = SpeakerPitchProportional.newInstance(speaker);
        } else if (speaker.getPitch().getSettings().getClass() == SettingsConstant.class) {
            result = SpeakerPitchConstant.newInstance(speaker);
        } else {
            Log.w(Constants.LOG_TAG, "SpeakerPitchStateHelper.newInstance: settings/relationship not implemented, returning null.");
            result = null;
        }

        return result;
    }

}
