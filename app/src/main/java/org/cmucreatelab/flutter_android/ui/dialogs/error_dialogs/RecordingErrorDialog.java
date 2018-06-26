package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class RecordingErrorDialog extends ErrorDialog {

    public static RecordingErrorDialog newInstance() {
        RecordingErrorDialog unsupportedBleErrorDialog = new RecordingErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.analog_or_unknown);
        args.putSerializable(ERROR_TEXT_KEY, R.string.analog_or_unknown);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_ble);

        unsupportedBleErrorDialog.setArguments(args);

        return unsupportedBleErrorDialog;
    }
    public void onClickToDismiss()
    {
        getActivity().finish();
    }
}
