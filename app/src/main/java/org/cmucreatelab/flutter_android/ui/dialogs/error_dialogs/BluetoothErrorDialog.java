package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Parv on 6/25/2018.
 *
 * BluetoothErrorDialog
 *
 * An error dialog that shows up when Bluetooth is currently disabled.
 *
 */

public class BluetoothErrorDialog extends ErrorDialog {

    private static BluetoothAdapter currentBluetoothAdapter;

    public static BluetoothErrorDialog newInstance(BluetoothAdapter adapter) {
        BluetoothErrorDialog bluetoothErrorDialog = new BluetoothErrorDialog();
        Bundle args = new Bundle();

        args.putSerializable(ERROR_TITLE_KEY, R.string.error_enable_bluetooth_title);
        args.putSerializable(ERROR_TEXT_KEY, R.string.error_enable_bluetooth_text);
        args.putSerializable(ERROR_IMAGE_KEY, R.drawable.error_ble);

        currentBluetoothAdapter = adapter;

        bluetoothErrorDialog.setArguments(args);

        return bluetoothErrorDialog;
    }

    public void onClickDismiss()
    {
        if (currentBluetoothAdapter.isEnabled()) {
            this.dismiss();
        }
    }
}
