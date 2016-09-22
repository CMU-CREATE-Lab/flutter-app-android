package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/22/2016.
 */
public class DialogFragmentColorLow extends DialogFragmentChooseColor {


    private DialogLowColorListener lowColorListener;


    public static DialogFragmentColorLow newInstance(Serializable serializable) {
        DialogFragmentColorLow dialogFragmentColorLow = new DialogFragmentColorLow();

        Bundle args = new Bundle();
        args.putSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY, serializable);
        dialogFragmentColorLow.setArguments(args);

        return dialogFragmentColorLow;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        lowColorListener = (DialogLowColorListener) getArguments().getSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY);
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetColor");
        lowColorListener.onLowColorChosen(finalRGB);
    }


    public interface DialogLowColorListener {
        public void onLowColorChosen(int[] color);
    }

}
