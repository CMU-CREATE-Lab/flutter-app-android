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
public class MaxVolumeDialog extends ChooseVolumeDialog implements ChooseVolumeDialog.SetVolumeListener {


    private static String VOLUME_KEY = "volume_key";
    private DialogMaxVolumeListener maxVolumeListener;


    public static MaxVolumeDialog newInstance(Serializable serializable) {
        MaxVolumeDialog maxVolumeDialog = new MaxVolumeDialog();

        Bundle args = new Bundle();
        args.putSerializable(VOLUME_KEY, serializable);
        maxVolumeDialog.setArguments(args);

        return maxVolumeDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        maxVolumeListener = (DialogMaxVolumeListener) getArguments().getSerializable(VOLUME_KEY);
        setVolumeListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetVolume() {
        Log.d(Constants.LOG_TAG, "onClickSetMaxVolume");
        maxVolumeListener.onMaxVolumeChosen(finalVolume);
        dismiss();
    }


    public interface DialogMaxVolumeListener {
        public void onMaxVolumeChosen(int max);
    }

}
