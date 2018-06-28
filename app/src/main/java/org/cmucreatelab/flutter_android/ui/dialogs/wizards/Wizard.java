package org.cmucreatelab.flutter_android.ui.dialogs.wizards;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by mike on 6/27/18.
 *
 * An abstract class that tracks the state of a step-by-step Wizard or setup assistant.
 */
public abstract class Wizard implements Serializable {

    private BaseResizableDialogWizardOld currentDialog;

    public abstract void start();

    public abstract void changeDialog(Bundle options);
//    public void changeDialog(BaseResizableDialogWizardOld dialog, Bundle options) {
//        // find the next dialog
//        // show next dialog
//        // dismiss current dialog
//    }

}
