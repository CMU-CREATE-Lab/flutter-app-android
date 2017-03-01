package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 3/1/2017.
 */

public class DataLoggingConfirmationSensor extends DataLoggingConfirmation {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        buttonOk.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_blue_button_bottom_right));

        return dialog;
    }
}
