package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;

/**
 * Created by mike on 4/6/17.
 *
 * LedDialogStateHelper implementation with Amplitude relationship
 *
 */
public class LedDialogAmplitude extends LedDialogStateHelperWithAdvancedSettings {


    LedDialogAmplitude(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogAmplitude newInstance(TriColorLed triColorLed) {
        return new LedDialogAmplitude(triColorLed);
    }

}
