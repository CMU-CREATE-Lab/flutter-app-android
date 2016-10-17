package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Steve on 10/17/2016.
 */
public class SpeakerDialog extends DialogFragment {


    public static SpeakerDialog newInstance() {
        SpeakerDialog speakerDialog = new SpeakerDialog();

        Bundle args = new Bundle();
        speakerDialog.setArguments(args);

        return speakerDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

}
