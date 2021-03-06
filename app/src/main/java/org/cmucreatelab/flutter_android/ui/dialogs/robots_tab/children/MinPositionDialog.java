package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 10/21/2016.
 *
 * MinPosition Dialog
 *
 * A Dialog that prompts the user to select a minimum Position of a servo.
 */
public class MinPositionDialog extends ChoosePositionDialog implements ChoosePositionDialog.SetPositionListener {

    private DialogMinPositionListener minPositionListener;


    public static MinPositionDialog newInstance(Integer position, Serializable serializable) {
        MinPositionDialog minPositionDialog = new MinPositionDialog();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_KEY, position);
        args.putSerializable(POSITION_LISTENER_KEY, serializable);
        minPositionDialog.setArguments(args);

        return minPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        minPositionListener = (DialogMinPositionListener) getArguments().getSerializable(POSITION_LISTENER_KEY);
        setPositionListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetPosition() {
        Log.d(Constants.LOG_TAG, "MinPositionDialog.onSetPosition");
        minPositionListener.onMinPosChosen(finalPosition);
        this.dismiss();
    }


    public interface DialogMinPositionListener {
        public void onMinPosChosen(int min);
    }

}
