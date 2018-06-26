package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class DataLogErrorDialog extends ErrorDialog {

    public static DataLogErrorDialog newInstance() {
        DataLogErrorDialog dataLogErrorDialog = new DataLogErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_data_log_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_data_log_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_ble);

        dataLogErrorDialog.setArguments(args);

        return dataLogErrorDialog;
    }
    public void onClickToDismiss()
    {
        getActivity().finish();
    }
}
