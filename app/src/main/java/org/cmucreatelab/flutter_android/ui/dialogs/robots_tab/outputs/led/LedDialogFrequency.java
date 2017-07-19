package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;

/**
 * Created by mike on 4/13/17.
 *
 * LedDialogStateHelper implementation with Frequency relationship
 *
 */
public class LedDialogFrequency extends LedDialogStateHelperWithAdvancedSettings {


    LedDialogFrequency(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogFrequency newInstance(TriColorLed triColorLed) {
        return new LedDialogFrequency(triColorLed);
    }

}
