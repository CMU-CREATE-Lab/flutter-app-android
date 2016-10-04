package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/16/2016.
 */
public class ColorHighDialogDialog extends ChooseColorDialog {

    private DialogHighColorListener highColorListener;


    public static ColorHighDialogDialog newInstance(Serializable serializable) {
        ColorHighDialogDialog colorHighDialog = new ColorHighDialogDialog();

        Bundle args = new Bundle();
        args.putSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY, serializable);
        colorHighDialog.setArguments(args);

        return colorHighDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        highColorListener = (DialogHighColorListener) getArguments().getSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY);
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetColor");
        highColorListener.onHighColorChosen(finalRGB);
    }


    public interface DialogHighColorListener {
        public void onHighColorChosen(int[] color);
    }

}
