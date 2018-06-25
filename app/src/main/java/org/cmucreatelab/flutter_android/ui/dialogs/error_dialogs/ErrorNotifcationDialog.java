package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;


import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohit on 6/12/2018.
 *
 * ErrorNotificationDialog
 *
 * A Dialog that shows a small pop up when there is something wrong with how the device is set up.
 * Supports all error messages. Just change imageView and TextView based on the error.
 */

// TODO @psdev1 treat this instead as an abstract class with each error type extending from ErrorNotifcationDialog
public class ErrorNotifcationDialog extends BaseOutputDialog implements View.OnClickListener {

    private static boolean bleError = false;
    private static boolean unsupportedBleDevice = false;

    // TODO @psdev1 remove static attributes and "id" arg in "newInstance" method.
    private static boolean emailError = false;
    private static boolean dataLogError = false;
    private static boolean recordingError = false;
    private static boolean unableToConnectError = false;
    private static boolean connectFlutterError = false;
    private static boolean largeScreenError = false;
    private static boolean connectToWifiError = false;
    private static boolean unknownDeviceError = false;
    private static BluetoothAdapter currentBlueTooth;

    public static ErrorNotifcationDialog newInstance(int id, BluetoothAdapter bluetooth) {
        if (id == 1) {
            bleError = true;
            currentBlueTooth = bluetooth;
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
        else if (id == 10) {
            unsupportedBleDevice = true;
        }
        ErrorNotifcationDialog errorNotifcationDialog = new ErrorNotifcationDialog();
        return errorNotifcationDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_screen_size_error, null);
        if (bleError) {
            ((TextView) view.findViewById(R.id.error_title)).setText("BLE");
            ((TextView) view.findViewById(R.id.text_error_description)).setText(getString(R.string.enable_bluetooth_msg));
            ((ImageView) view.findViewById(R.id.error_image)).setImageResource(R.drawable.error_ble);
        }
        if (unsupportedBleDevice) {
            ((TextView) view.findViewById(R.id.error_title)).setText("BLE");
            ((TextView) view.findViewById(R.id.text_error_description)).setText(getString(R.string.ble_unsupported));
            ((ImageView) view.findViewById(R.id.error_image)).setImageResource(R.drawable.error_ble);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();
    }


    @Override
    public void onClick(View view) {
        //this.dismiss();
    }

    @OnClick(R.id.button_accept)
    public void onClickToDismiss() {
        if (bleError) {
            if (currentBlueTooth.isEnabled()) {
                this.dismiss();
            }
        }
        else if (unsupportedBleDevice) {
            getActivity().finish();
        }
        else {
            this.dismiss();
        }
    }



}
