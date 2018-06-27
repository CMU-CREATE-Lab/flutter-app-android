package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class DataLogErrorDialog extends ErrorDialog {

    public static DataLogErrorDialog newInstance(DataLogErrorTypes dataLogErrorTypes) {
        DataLogErrorDialog dataLogErrorDialog = new DataLogErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_data_log_title);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_data_log);

        switch (dataLogErrorTypes)
        {
            case NONE_AVAILABLE_CLEAN_UP:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_data_logs_to_clean_up_details);
                break;
            case NONE_AVAILABLE_OPEN:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_data_logs_to_open_details);
                break;
            case MUST_HAVE_SELECTED:
                args.putSerializable(ERROR_TEXT_KEY, R.string.select_a_data_log_details);
                break;
        }

        dataLogErrorDialog.setArguments(args);

        return dataLogErrorDialog;
    }
    public void onClickDismiss()
    {
        this.dismiss();
    }

    public enum DataLogErrorTypes {
        MUST_HAVE_SELECTED, NONE_AVAILABLE_CLEAN_UP, NONE_AVAILABLE_OPEN
    }
}
