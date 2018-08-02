package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;

import butterknife.ButterKnife;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        Dialog dialog = super.onCreateDialog(savedInstances);
        audioPlayer.addAudio(R.raw.a_03);
        audioPlayer.playAudio();
        return dialog;
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