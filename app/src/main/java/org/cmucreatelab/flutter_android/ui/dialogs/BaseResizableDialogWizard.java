package org.cmucreatelab.flutter_android.ui.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

/* Created by Mohit.
Just simply changed the number of pixels for the dialog size.
*/

public class BaseResizableDialogWizard extends DialogFragment{
    protected int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(459), ViewGroup.LayoutParams.WRAP_CONTENT);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
