package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 5/1/2017.
 *
 * ErrorDisconnectedDialog
 *
 * A Dialog for BLEError.Type.TIMEOUT_ERROR
 */
public class ErrorDisconnectedDialog extends BaseResizableDialog {


    private static ErrorDisconnectedDialog newInstance() {
        ErrorDisconnectedDialog dialog = new ErrorDisconnectedDialog();

        Bundle args = new Bundle();
        dialog.setCancelable(false);
        dialog.setArguments(args);

        return dialog;
    }


    public static void displayDialog(BaseNavigationActivity activity) {
        ErrorDisconnectedDialog dialog = ErrorDisconnectedDialog.newInstance();
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_error_disconnected, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        Button connectFlutterButton = (Button) view.findViewById(R.id.button_connect_flutter);
        String activityName = getActivity().getLocalClassName();

        if (activityName.equals("activities.SensorsActivity")) {
            connectFlutterButton.setBackgroundResource(R.drawable.round_blue_button_bottom);
        } else if (activityName.equals("activities.RobotActivity")) {
            connectFlutterButton.setBackgroundResource(R.drawable.round_green_button_bottom);
        } else if (activityName.equals("activities.DataLogsActivity")) {
            connectFlutterButton.setBackgroundResource(R.drawable.round_orange_button_bottom);
        }

        return builder.create();
    }


    @OnClick(R.id.button_connect_flutter)
    public void onClickConnectFlutter() {
        Log.d(Constants.LOG_TAG, "ErrorDisconnectedDialog.onClickConnectFlutter");
        GlobalHandler.getInstance(getActivity().getApplicationContext()).melodySmartDeviceHandler.disconnect(false);
    }

}
