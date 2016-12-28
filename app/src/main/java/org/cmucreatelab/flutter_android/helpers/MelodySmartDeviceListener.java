package org.cmucreatelab.flutter_android.helpers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartListener;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 12/28/16.
 */

public class MelodySmartDeviceListener implements MelodySmartListener {

    private Session mSession;
    public boolean deviceConnected;


    public MelodySmartDeviceListener(Session session) {
        this.mSession = session;
        deviceConnected = false;
    }


    @Override
    public void onDeviceConnected() {
        Log.v(Constants.LOG_TAG, "MelodySmartDeviceListener.onDeviceConnected");
        deviceConnected = true;
    }


    @Override
    public void onDeviceDisconnected(final BLEError bleError) {
        Log.v(Constants.LOG_TAG, "MelodySmartDeviceListener.onDeviceDisconnected");
        deviceConnected = false;

        // Check for errors
        if (bleError.getType() != BLEError.Type.NO_ERROR) {
            mSession.currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(mSession.currentActivity, R.style.AppTheme));
                    adb.setMessage(bleError.getMessage());
                    adb.setTitle("Disconnected");
                    adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSession.flutterConnectListener.onFlutterDisconnected();
                        }
                    });
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            });
        } else {
            mSession.flutterConnectListener.onFlutterDisconnected();
        }
    }

    public void onOtauAvailable() {}
    public void onOtauRecovery(DeviceDatabase.DeviceData deviceData) {}

}
