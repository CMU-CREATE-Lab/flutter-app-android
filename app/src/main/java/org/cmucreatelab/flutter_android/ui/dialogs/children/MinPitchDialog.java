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
public class MinPitchDialog extends ChoosePitchDialog {


    private static String PITCH_KEY = "pitch_key";
    private DialogMinPitchListener minPitchListener;


    public static MinPitchDialog newInstance(Serializable serializable) {
        MinPitchDialog minPitchDialog = new MinPitchDialog();

        Bundle args = new Bundle();
        args.putSerializable(PITCH_KEY, serializable);
        minPitchDialog.setArguments(args);

        return minPitchDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        minPitchListener = (DialogMinPitchListener) getArguments().getSerializable(PITCH_KEY);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPitch");
        minPitchListener.onMinPitchChosen(finalPitch);
    }


    public interface DialogMinPitchListener {
        public void onMinPitchChosen(int min);
    }

}
