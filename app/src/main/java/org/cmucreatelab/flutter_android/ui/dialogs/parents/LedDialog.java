package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Steve on 10/17/2016.
 */
public class LedDialog extends DialogFragment {


    public static LedDialog newInstance() {
        LedDialog ledDialog = new LedDialog();

        Bundle args = new Bundle();
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

}
