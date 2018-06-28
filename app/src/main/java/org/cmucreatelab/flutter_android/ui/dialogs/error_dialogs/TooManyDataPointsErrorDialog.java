package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by parvs on 6/25/2018.
 *
 *
 *
 */
public class TooManyDataPointsErrorDialog extends ErrorConfirmationDialog {
    static DismissWarningListener dismissAndCancelWarningListener;

    public static TooManyDataPointsErrorDialog newInstance(DismissWarningListener dismissWarningListener) {
        TooManyDataPointsErrorDialog tooManyDataPointsErrorDialog = new TooManyDataPointsErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_data_log_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.a_lot_of_data_points_details);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_data_log);

        tooManyDataPointsErrorDialog.setArguments(args);

        dismissAndCancelWarningListener = dismissWarningListener;

        return tooManyDataPointsErrorDialog;
    }

    public void onClickConfirm() {
        dismissAndCancelWarningListener.onOkButton();
        dismiss();
    }

    public void onClickCancel() {
        this.dismiss();
    }


    /**
     * This defines what should happen on dismiss and in onCancel.
     * This listener is not required.
     */
    public interface DismissWarningListener extends Serializable {
        void onOkButton();
    }
}
