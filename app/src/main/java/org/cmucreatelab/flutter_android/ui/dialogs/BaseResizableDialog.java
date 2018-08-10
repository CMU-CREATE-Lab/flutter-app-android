package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.helpers.FlutterAudioPlayer;

/**
 * Created by Steve on 11/4/2016.
 *
 * BaseResizableDialog
 *
 * Dialog that is resized to a set width and has a transparent background for the rounded dialogs.
 */
public abstract class BaseResizableDialog extends DialogFragment {
    protected FlutterAudioPlayer flutterAudioPlayer;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        flutterAudioPlayer = FlutterAudioPlayer.getInstance(getActivity());
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
