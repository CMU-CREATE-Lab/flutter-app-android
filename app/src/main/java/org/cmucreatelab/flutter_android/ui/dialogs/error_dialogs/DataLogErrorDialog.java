package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Parv on 6/25/2018.
 *
 * DataLogErrorDialog
 *
 * An error dialog for various errors that can occur in the data logs tab.
 */

public class DataLogErrorDialog extends ErrorDialog {
    private DataLogErrorType dataLogErrorType;


    public static DataLogErrorDialog newInstance(DataLogErrorType dataLogErrorType) {
        DataLogErrorDialog dataLogErrorDialog = new DataLogErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_data_log_title);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_data_log);

        switch (dataLogErrorType) {
            case NONE_AVAILABLE_CLEAN_UP:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_data_logs_to_clean_up_details);
                break;
            case NONE_AVAILABLE_OPEN:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_data_logs_to_open_details);
                break;
            case MUST_HAVE_SELECTED:
                args.putSerializable(ERROR_TEXT_KEY, R.string.select_a_data_log_details);
                break;
            case TOO_MUCH_SAMPLES:
                args.putSerializable(ERROR_TEXT_KEY, R.string.too_much_samples);
                break;
        }

        dataLogErrorDialog.dataLogErrorType = dataLogErrorType;

        dataLogErrorDialog.setArguments(args);

        return dataLogErrorDialog;
    }


    @Override
    public void playAudio() {
        switch (dataLogErrorType) {
            case MUST_HAVE_SELECTED:
                flutterAudioPlayer.addAudio(R.raw.audio_36);
                flutterAudioPlayer.playAudio();
                break;
        }

        flutterAudioPlayer.playAudio();
    }


    public void onClickDismiss() {
        super.onClickDismiss();
        this.dismiss();
    }

<<<<<<< HEAD

    public enum DataLogErrorType {
        MUST_HAVE_SELECTED, NONE_AVAILABLE_CLEAN_UP, NONE_AVAILABLE_OPEN
=======
    public enum DataLogErrorTypes {
        MUST_HAVE_SELECTED, NONE_AVAILABLE_CLEAN_UP, NONE_AVAILABLE_OPEN, TOO_MUCH_SAMPLES
>>>>>>> 07f87bc1f11eaa13f48f27ffd9a5c883200ca932
    }
}
