package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 11/14/2016.
 */
public class MinPitchDialog extends ChoosePitchDialog implements ChoosePitchDialog.SetPitchListener {

    private DialogMinPitchListener minPitchListener;


    public static MinPitchDialog newInstance(Integer pitch, Serializable serializable) {
        MinPitchDialog minPitchDialog = new MinPitchDialog();

        Bundle args = new Bundle();
        args.putSerializable(PITCH_KEY, pitch);
        args.putSerializable(PITCH_LISTENER_KEY, serializable);
        minPitchDialog.setArguments(args);

        return minPitchDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        minPitchListener = (DialogMinPitchListener) getArguments().getSerializable(PITCH_LISTENER_KEY);
        setPitchListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetPitch() {
        Log.d(Constants.LOG_TAG, "MinPitchDialog.onSetPitch");
        minPitchListener.onMinPitchChosen(finalPitch);
        dismiss();
    }


    public interface DialogMinPitchListener {
        public void onMinPitchChosen(int min);
    }

}
