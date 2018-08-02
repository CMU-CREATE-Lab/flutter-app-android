package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.helpers.AudioPlayer;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

/**
 * Created by Steve on 11/4/2016.
 */
public abstract class BaseResizableDialog extends DialogFragment {
    protected AudioPlayer audioPlayer;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        audioPlayer = AudioPlayer.getInstance(getActivity());
        return super.onCreateDialog(savedInstanceState);
    }

    protected int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(convertDpToPx(350), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
