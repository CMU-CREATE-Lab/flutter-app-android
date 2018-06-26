package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class LargeScreenErrorDialog extends ErrorDialog {

    public static LargeScreenErrorDialog newInstance() {
        LargeScreenErrorDialog unsupportedBleErrorDialog = new LargeScreenErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_large_screen_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_large_screen_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_large_screen);

        unsupportedBleErrorDialog.setArguments(args);

        return unsupportedBleErrorDialog;
    }
    public void onClickToDismiss()
    {
        getActivity().finish();
    }
}
