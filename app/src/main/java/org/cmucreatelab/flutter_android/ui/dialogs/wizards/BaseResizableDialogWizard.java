package org.cmucreatelab.flutter_android.ui.dialogs.wizards;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ServoWizard;

/* Created by Mohit.
Just simply changed the number of pixels for the dialog size.
*/

public class BaseResizableDialogWizard extends BaseResizableDialog {

    public ServoWizard wizard;
    public static String KEY_WIZARD = "key_wizard";


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(500), ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.wizard = (ServoWizard)(getArguments().getSerializable(KEY_WIZARD));
        return super.onCreateDialog(savedInstanceState);
    }

}
