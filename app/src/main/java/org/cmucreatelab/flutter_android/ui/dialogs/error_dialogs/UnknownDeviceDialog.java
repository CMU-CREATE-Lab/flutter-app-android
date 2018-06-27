package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class UnknownDeviceDialog extends ErrorDialog {

    public static UnknownDeviceDialog newInstance() {
        UnknownDeviceDialog unknownDeviceDialog = new UnknownDeviceDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_unknown_device_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_unknown_device_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_unknown_device);

        unknownDeviceDialog.setArguments(args);

        return unknownDeviceDialog;
    }
    public void onClickDismiss()
    {
        dismiss();
    }
}
