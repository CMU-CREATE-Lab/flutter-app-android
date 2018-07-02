package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.content.Intent;
import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;

/**
 * Created by Parv on 6/25/2018.
 *
 * UnableToConnectFlutterDialog
 *
 * An error dialog that displays when the flutter is unable to connect or is
 * disconnected from the tablet.
 *
 */

public class UnableToConnectFlutterDialog extends ErrorDialog {

    public static UnableToConnectFlutterDialog newInstance(FlutterIssueType flutterIssueType) {
        UnableToConnectFlutterDialog unableToConnectFlutterDialog = new UnableToConnectFlutterDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_unable_to_connect_title);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_unable_to_connect);
        args.putSerializable(BUTTON_TEXT_KEY, R.string.not_connected_button);

        switch (flutterIssueType)
        {
            case TIMEOUT_DISCONNECTED:
            case INDETERMINATE:
                args.putSerializable(ERROR_TEXT_KEY, R.string.disconnected_message);
                break;
            case UNKNOWN_NOT_CONNECTED:
                args.putSerializable(ERROR_TEXT_KEY, R.string.not_connected_message);
                break;
        }

        unableToConnectFlutterDialog.setArguments(args);

        return unableToConnectFlutterDialog;
    }

    public void onClickDismiss() {
        Intent intent = new Intent(getActivity(), AppLandingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    public enum FlutterIssueType {
        TIMEOUT_DISCONNECTED, UNKNOWN_NOT_CONNECTED, INDETERMINATE
    }
}