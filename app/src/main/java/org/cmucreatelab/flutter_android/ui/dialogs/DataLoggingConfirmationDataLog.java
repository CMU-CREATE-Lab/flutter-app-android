package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 3/1/2017.
 */

public class DataLoggingConfirmationDataLog extends DataLoggingConfirmation {

    private static final String DISMISS_KEY = "dismiss_key";

    private DismissDialogListener dismissDialogListener;


    public static DataLoggingConfirmationDataLog newInstance(Serializable serializable) {
        DataLoggingConfirmationDataLog dataLoggingConfirmationDataLog = new DataLoggingConfirmationDataLog();

        Bundle args = new Bundle();
        args.putSerializable(DISMISS_KEY, serializable);
        dataLoggingConfirmationDataLog.setArguments(args);

        return dataLoggingConfirmationDataLog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DISMISS_KEY);
        buttonOk.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_orange_button_bottom_right));
        return dialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissDialogListener.onDialogDismissed();
    }
}
