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
public class MinPositionDialog extends MaxMinPositionDialog {


    private static String POSITION_KEY = "position_key";
    private DialogMinPositionListener minPositionListener;


    public static MinPositionDialog newInstance(Serializable serializable) {
        MinPositionDialog minPositionDialog = new MinPositionDialog();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_KEY, serializable);
        minPositionDialog.setArguments(args);

        return minPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        minPositionListener = (DialogMinPositionListener) getArguments().getSerializable(POSITION_KEY);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetMinPosition");
        minPositionListener.onMinPosChosen(finalPosition);
    }


    public interface DialogMinPositionListener {
        public void onMinPosChosen(int min);
    }

}
