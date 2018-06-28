package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

/**
 * Created by parvs on 6/25/2018.
 */

public class StopRecordingErrorDialog extends ErrorConfirmationDialog {

    public static StopRecordingErrorDialog newInstance() {
        StopRecordingErrorDialog stopRecordingErrorDialog = new StopRecordingErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_recording_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.overwrite_message);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_recording);
        args.putSerializable(BUTTON_TEXT_KEY, R.string.stop_the_current_recoring);

        stopRecordingErrorDialog.setArguments(args);

        return stopRecordingErrorDialog;
    }

    public void onClickConfirm()
    {
        GlobalHandler.getInstance(getActivity()).dataLoggingHandler.stopRecording();
        this.dismiss();
    }

    public void onClickCancel()
    {
        this.dismiss();
    }
}
