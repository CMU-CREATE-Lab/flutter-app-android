package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;

/**
 * Created by mike on 4/13/17.
 *
 * LedDialogStateHelper implementation with Change relationship
 *
 */
public class LedDialogChange extends LedDialogStateHelperWithAdvancedSettings {


    LedDialogChange(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogChange newInstance(TriColorLed triColorLed) {
        return new LedDialogChange(triColorLed);
    }

}
