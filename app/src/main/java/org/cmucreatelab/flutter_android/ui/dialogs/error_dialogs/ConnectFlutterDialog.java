package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.content.Intent;
import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;

/**
 * Created by Parv on 6/25/2018.
 *
 * ConnectFlutterDialog
 *
 * An error dialog that shows up when the flutter needs to be connected.
 *
 */

public class ConnectFlutterDialog extends ErrorDialog {

    public static ConnectFlutterDialog newInstance(ConnectFlutterPreviousScreen screenfromDialogLaunch) {
        ConnectFlutterDialog connectFlutterDialog = new ConnectFlutterDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.connect_flutter);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_connect_flutter);
        args.putSerializable(BUTTON_TEXT_KEY, R.string.connect_flutter);

        switch (screenfromDialogLaunch)
        {
            case ROBOT:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_flutter_robot);
                break;
            case SENSORS:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_flutter_sensor);
                break;
            case RECORD_DATA:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_flutter_data_logs);
                break;
            default:
                args.putSerializable(ERROR_TEXT_KEY, R.string.no_flutter_default);
                break;
        }

        connectFlutterDialog.setArguments(args);

        return connectFlutterDialog;
    }

    public void onClickDismiss() {
        Intent intent = new Intent(getActivity(), AppLandingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    public enum ConnectFlutterPreviousScreen {
        ROBOT, SENSORS, RECORD_DATA, GENERAL
    }
}