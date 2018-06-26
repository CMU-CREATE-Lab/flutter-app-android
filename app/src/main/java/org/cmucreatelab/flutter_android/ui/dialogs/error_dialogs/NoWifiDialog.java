package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class NoWifiDialog extends ErrorDialog {

    public static NoWifiDialog newInstance() {
        NoWifiDialog noWifiDialog = new NoWifiDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_no_wifi_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_no_wifi_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_wifi);

        noWifiDialog.setArguments(args);

        return noWifiDialog;
    }
    public void onClickToDismiss()
    {
        dismiss();
    }
}
