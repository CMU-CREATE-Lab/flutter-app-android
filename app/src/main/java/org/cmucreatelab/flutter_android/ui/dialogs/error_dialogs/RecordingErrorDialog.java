package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Parv on 6/25/2018.
 *
 * RecordingErrorDialog
 *
 * An error dialog that shows when a user tries to record a second time for data logs.
 */

public class RecordingErrorDialog extends ErrorDialog {

    public static RecordingErrorDialog newInstance() {
        RecordingErrorDialog recordingErrorDialog = new RecordingErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_recording_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.currently_recording_description);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_recording);

        recordingErrorDialog.setArguments(args);

        return recordingErrorDialog;
    }

    @Override
    public void playAudio() {
        flutterAudioPlayer.addAudio(R.raw.audio_27);
        flutterAudioPlayer.playAudio();
    }


    public void onClickDismiss() {
        super.onClickDismiss();
        this.dismiss();
    }
}
