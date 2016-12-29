package org.cmucreatelab.flutter_android.helpers.melodysmart.listeners;

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
 *
 * Creates a MelodySmartListener instance using information from a Session.
 *
 */
public class DeviceListener implements MelodySmartListener {

    private Session session;
    private boolean deviceConnected;


    public boolean isDeviceConnected() {
        return deviceConnected;
    }


    public DeviceListener(Session session) {
        this.session = session;
        deviceConnected = false;
    }


    @Override
    public void onDeviceConnected() {
        Log.v(Constants.LOG_TAG, "DeviceListener.onDeviceConnected");
        deviceConnected = true;
    }


    @Override
    public void onDeviceDisconnected(final BLEError bleError) {
        Log.v(Constants.LOG_TAG, "DeviceListener.onDeviceDisconnected");
        deviceConnected = false;

        // Check for errors
        if (bleError.getType() != BLEError.Type.NO_ERROR) {
            session.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(session.getCurrentActivity(), R.style.AppTheme));
                    adb.setMessage(bleError.getMessage());
                    adb.setTitle("Disconnected");
                    adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            session.getFlutterConnectListener().onFlutterDisconnected();
                        }
                    });
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            });
        } else {
            session.getFlutterConnectListener().onFlutterDisconnected();
        }
    }


    // unused implementations
    public void onOtauAvailable() {}
    public void onOtauRecovery(DeviceDatabase.DeviceData deviceData) {}

}
