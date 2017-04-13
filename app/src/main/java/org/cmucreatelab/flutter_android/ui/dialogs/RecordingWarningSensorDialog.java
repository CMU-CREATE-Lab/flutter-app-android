package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 2/27/2017.
 */

public class RecordingWarningSensorDialog extends RecordingWarningDialog {


    public static RecordingWarningSensorDialog newInstance(Serializable serializable, String name, int times, String time, int forTimes, String forTime) {
        RecordingWarningSensorDialog recordingWarningSensorDialog = new RecordingWarningSensorDialog();

        Bundle args = new Bundle();
        args.putSerializable(DismissDialogListener.DISMISS_KEY, serializable);
        args.putString(NAME_KEY, name);
        args.putInt(TIMES_KEY, times);
        args.putString(TIME_KEY, time);
        args.putInt(FOR_TIMES_KEY, forTimes);
        args.putString(FOR_TIME_KEY, forTime);
        recordingWarningSensorDialog.setArguments(args);

        return recordingWarningSensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "RecordingWarningSensorDialog.onCreateDialog");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        buttonOk.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_blue_button_bottom_right));
        return dialog;
    }

}
