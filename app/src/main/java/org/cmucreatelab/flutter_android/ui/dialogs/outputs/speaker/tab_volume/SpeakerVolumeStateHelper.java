package org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.tab_volume;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerStateHelper;

/**
 * Created by mike on 3/27/17.
 *
 * SpeakerStateHelper when the Volume tab is selected
 *
 */
public abstract class SpeakerVolumeStateHelper extends SpeakerStateHelper {


    SpeakerVolumeStateHelper(Speaker speaker) {
        super(TabType.VOLUME, speaker);
    }


    public static SpeakerVolumeStateHelper newInstance(Speaker speaker) {
        SpeakerVolumeStateHelper result;

        if (speaker.getVolume().getSettings().getClass() == SettingsProportional.class) {
            result = SpeakerVolumeProportional.newInstance(speaker);
        } else if (speaker.getVolume().getSettings().getClass() == SettingsConstant.class) {
            result = SpeakerVolumeConstant.newInstance(speaker);
        } else if (speaker.getVolume().getSettings().getClass() == SettingsAmplitude.class) {
            result = SpeakerVolumeAmplitude.newInstance(speaker);
        } else {
            Log.w(Constants.LOG_TAG, "SpeakerVolumeStateHelper.newInstance: settings/relationship not implemented, returning null.");
            result = null;
        }

        return result;
    }

}
