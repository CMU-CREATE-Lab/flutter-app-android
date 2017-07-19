package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;

/**
 * Created by mike on 4/13/17.
 *
 * LedDialogStateHelper implementation with Cumulative relationship
 *
 */
public class LedDialogCumulative extends LedDialogStateHelperWithAdvancedSettings {


    LedDialogCumulative(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogCumulative newInstance(TriColorLed triColorLed) {
        return new LedDialogCumulative(triColorLed);
    }

}
