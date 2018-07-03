package org.cmucreatelab.flutter_android.ui.dialogs.wizards;

import java.io.Serializable;

/**
 * Created by mike on 6/27/18.
 *
 * An abstract class that tracks the state of a step-by-step Wizard or setup assistant.
 */
public abstract class Wizard implements Serializable {

    public abstract void start();

    public abstract void finish();

    public abstract void changeDialog(BaseResizableDialogWizard nextDialog);

}
