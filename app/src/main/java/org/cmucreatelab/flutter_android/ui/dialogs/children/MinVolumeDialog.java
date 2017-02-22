package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 11/14/2016.
 */
public class MinVolumeDialog extends ChooseVolumeDialog implements ChooseVolumeDialog.SetVolumeListener {

    private DialogMinVolumeListener minVolumeListener;


    public static MinVolumeDialog newInstance(Integer volume, Serializable serializable) {
        MinVolumeDialog minVolumeDialog = new MinVolumeDialog();

        Bundle args = new Bundle();
        args.putSerializable(VOLUME_KEY, volume);
        args.putSerializable(VOLUME_LISTENER_KEY, serializable);
        minVolumeDialog.setArguments(args);

        return minVolumeDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        minVolumeListener = (DialogMinVolumeListener) getArguments().getSerializable(VOLUME_LISTENER_KEY);
        setVolumeListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetVolume() {
        Log.d(Constants.LOG_TAG, "onClickSetMinVolume");
        minVolumeListener.onMinVolumeChosen(finalVolume);
        dismiss();
    }


    public interface DialogMinVolumeListener {
        public void onMinVolumeChosen(int min);
    }

}
