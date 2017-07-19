package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;

/**
 * Created by mike on 3/27/17.
 *
 * LedDialogStateHelper implementation with Proportional relationship
 *
 */
public class LedDialogProportional extends LedDialogStateHelperWithAdvancedSettings {


    LedDialogProportional(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogProportional newInstance(TriColorLed triColorLed) {
        return new LedDialogProportional(triColorLed);
    }

}
