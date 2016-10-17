package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Steve on 10/17/2016.
 */
public class ServoDialog extends DialogFragment {


    public static ServoDialog newInstance() {
        ServoDialog servoDialog = new ServoDialog();

        Bundle args = new Bundle();
        servoDialog.setArguments(args);

        return servoDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

}
