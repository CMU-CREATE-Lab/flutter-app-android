package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;


import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Mohit on 6/12/2018.
 *
 * ErrorNotificationDialog
 *
 * A Dialog that shows a small pop up when there is something wrong with how the device is set up.
 * Supports all error messages. Just change imageView and TextView based on the error.
 */

public class ErrorNotifcationDialog extends BaseResizableDialogWizard implements View.OnClickListener {

    private static boolean bleError = false;
    private static boolean emailError = false;
    private static boolean dataLogError = false;
    private static boolean recordingError = false;
    private static boolean unableToConnectError = false;
    private static boolean connectFlutterError = false;
    private static boolean largeScreenError = false;
    private static boolean connectToWifiError = false;
    private static boolean unknownDeviceError = false;

    public static ErrorNotifcationDialog newInstance(int id) {
        if (id == 1) {
            bleError = true;
        }
        else if (id == 2) {
            emailError = true;
        }
        else if (id == 3) {
            dataLogError = true;
        }
        else if (id == 4) {
            recordingError = true;
        }
        else if (id == 5) {
            unableToConnectError = true;
        }
        else if (id == 6) {
            connectFlutterError = true;
        }
        else if (id == 7) {
            largeScreenError = true;
        }
        else if (id == 8) {
            connectToWifiError = true;
        }
        else if (id == 9) {
            unknownDeviceError = true;
        }
        ErrorNotifcationDialog errorNotifcationDialog = new ErrorNotifcationDialog();
        return errorNotifcationDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_screen_size_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();
    }


    @Override
    public void onClick(View view) {
        this.dismiss();
    }

    @OnClick(R.id.button_accept)
    public void onClickToDismiss() {
        this.dismiss();
    }



}

