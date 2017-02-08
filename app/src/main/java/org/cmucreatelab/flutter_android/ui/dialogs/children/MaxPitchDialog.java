package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 11/14/2016.
 */
public class MaxPitchDialog extends ChoosePitchDialog implements ChoosePitchDialog.SetPitchListener {


    private static String PITCH_KEY = "pitch_key";
    private DialogMaxPitchListener maxPitchListener;


    public static MaxPitchDialog newInstance(Serializable serializable) {
        MaxPitchDialog maxPitchDialog = new MaxPitchDialog();

        Bundle args = new Bundle();
        args.putSerializable(PITCH_KEY, serializable);
        maxPitchDialog.setArguments(args);

        return maxPitchDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        maxPitchListener = (DialogMaxPitchListener) getArguments().getSerializable(PITCH_KEY);
        setPitchListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetPitch() {
        Log.d(Constants.LOG_TAG, "MaxPitchDialog.onSetPitch");
        maxPitchListener.onMaxPitchChosen(finalPitch);
        dismiss();
    }


    public interface DialogMaxPitchListener {
        public void onMaxPitchChosen(int max);
    }

}
