package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;

/**
 * Created by Steve on 11/2/2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }

}
