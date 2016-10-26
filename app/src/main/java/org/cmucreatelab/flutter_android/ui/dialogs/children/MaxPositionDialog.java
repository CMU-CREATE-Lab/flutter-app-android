package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 10/21/2016.
 */
public class MaxPositionDialog extends MaxMinPositionDialog {


    private static String POSITION_KEY = "position_key";
    private DialogMaxPositionListener maxPositionListener;


    public static MaxPositionDialog newInstance(Serializable serializable) {
        MaxPositionDialog maxPositionDialog = new MaxPositionDialog();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_KEY, serializable);
        maxPositionDialog.setArguments(args);

        return maxPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        maxPositionListener = (DialogMaxPositionListener) getArguments().getSerializable(POSITION_KEY);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPosition");
        maxPositionListener.onMaxPosChosen(finalPosition);
    }


    public interface DialogMaxPositionListener {
        public void onMaxPosChosen(int max);
    }

}
