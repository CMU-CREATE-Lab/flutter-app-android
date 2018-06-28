package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by parvs on 6/25/2018.
 */

public class EmailErrorDialog extends ErrorDialog {

    public static EmailErrorDialog newInstance(EmailErrorType emailErrorType) {
        EmailErrorDialog emailErrorDialog = new EmailErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_email_server_title);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_email);

        switch (emailErrorType) {
            case GENERAL:
                args.putSerializable(ERROR_TEXT_KEY, R.string.error_email_server_text);
                break;
            case NO_EMAIL_APP:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_mail_app);
                break;
        }

        emailErrorDialog.setArguments(args);

        return emailErrorDialog;
    }

    public void onClickDismiss()
    {
        dismiss();
    }

    public enum EmailErrorType {
        GENERAL, NO_EMAIL_APP
    }
}
