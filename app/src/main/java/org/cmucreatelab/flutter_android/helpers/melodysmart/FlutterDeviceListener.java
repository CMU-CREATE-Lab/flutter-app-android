package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;

import org.cmucreatelab.android.melodysmart.listeners.DeviceListener;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.ErrorDisconnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.ErrorNotConnectedDialog;

/**
 * Created by mike on 2/9/17.
 */
public class FlutterDeviceListener extends DeviceListener {

    final private Session session;


    public FlutterDeviceListener(Session session) {
        this.session = session;
    }


    @Override
    public void onConnected() {
        Log.v(Constants.LOG_TAG, "FlutterDeviceListener.onConnected");
    }


    @Override
    public void onDisconnected(final BLEError bleError) {
        Log.v(Constants.LOG_TAG, "FlutterDeviceListener.onDisconnected");
        GlobalHandler.getInstance(session.getCurrentActivity()).melodySmartDeviceHandler.clearMessages();

        // Check for errors
        if (bleError.getType() != BLEError.Type.NO_ERROR) {
            session.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bleError.getType() == BLEError.Type.UNKNOWN_ERROR) {
                        ErrorNotConnectedDialog.displayDialog(session.getCurrentActivity());
                    } else if (bleError.getType() == BLEError.Type.TIMEOUT_ERROR) {
                        ErrorDisconnectedDialog.displayDialog(session.getCurrentActivity());
                    } else {
                        Log.w(Constants.LOG_TAG, "Could not determine BLEError.Type; defaulting to generic popup with unhelpful error message.");
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
                }
            });
        } else {
            session.getFlutterConnectListener().onFlutterDisconnected();
            Intent intent = new Intent(session.getCurrentActivity(), AppLandingActivity.class);
            session.getCurrentActivity().startActivity(intent);
        }
    }

}
