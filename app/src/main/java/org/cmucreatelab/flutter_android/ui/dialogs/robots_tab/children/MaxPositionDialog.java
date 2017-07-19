package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 10/21/2016.
 *
 * MaxPositionDialog
 *
 * A Dialog that prompts the user to choose a max position for a servo.
 */
public class MaxPositionDialog extends ChoosePositionDialog implements ChoosePositionDialog.SetPositionListener {

    private DialogMaxPositionListener maxPositionListener;


    public static MaxPositionDialog newInstance(Integer position, Serializable serializable) {
        MaxPositionDialog maxPositionDialog = new MaxPositionDialog();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_KEY, position);
        args.putSerializable(POSITION_LISTENER_KEY, serializable);
        maxPositionDialog.setArguments(args);

        return maxPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        maxPositionListener = (DialogMaxPositionListener) getArguments().getSerializable(POSITION_LISTENER_KEY);
        setPositionListener = this;
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onSetPosition() {
        Log.d(Constants.LOG_TAG, "MaxPositionDialog.onSetPosition");
        maxPositionListener.onMaxPosChosen(finalPosition);
        this.dismiss();
    }


    public interface DialogMaxPositionListener {
        public void onMaxPosChosen(int max);
    }

}
