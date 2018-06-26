package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class UnsupportedBleErrorDialog extends ErrorDialog {

    public static UnsupportedBleErrorDialog newInstance() {
        UnsupportedBleErrorDialog unsupportedBleErrorDialog = new UnsupportedBleErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_ble_unsupported_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_ble_unsupported_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_ble);

        unsupportedBleErrorDialog.setArguments(args);

        return unsupportedBleErrorDialog;
    }
    public void onClickToDismiss()
    {
        getActivity().finish();
    }
}
