package org.cmucreatelab.flutter_android.ui.dialogs.wizards;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.ServoWizard;

/* Created by Mohit.
Just simply changed the number of pixels for the dialog size.
*/

// TODO @tasota extend BaseResizableDialog
public class BaseResizableDialogWizard extends DialogFragment {

    private ServoWizard wizard;
    public static String KEY_WIZARD = "key_wizard";


    protected int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(500), ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.wizard = (ServoWizard)(getArguments().getSerializable(KEY_WIZARD));
        return super.onCreateDialog(savedInstanceState);
    }

//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//    }
    public void changeDialog(Bundle options) {
        wizard.changeDialog(options);
    }
}
