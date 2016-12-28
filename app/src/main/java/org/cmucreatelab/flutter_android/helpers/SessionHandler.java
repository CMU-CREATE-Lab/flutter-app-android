package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartListener;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/31/2016.
 *
 * Session Handler
 *
 * A class that handles your session (when you select a specific device on AppLandingActivity)
 * - connect
 * - disconnectFlutter
 * - message sending
 *
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    private Activity mActivity;
    private FlutterConnectListener flutterConnectListener;  // an interface that an activity will implement so we can have a method to callback after a connection/disconnection happened
    private FlutterMessageListener flutterMessageListener;  // an interface that an activity will implement so we can have a method to callback once a message has been sent
    private FlutterOG mFlutterOG;
    public boolean isBluetoothConnected;
    private ConcurrentLinkedQueue<String> messages;


    private MelodySmartListener melodySmartListener = new MelodySmartListener() {
        @Override
        public void onDeviceConnected() {
            Log.d(Constants.LOG_TAG, "Connecting to " + mFlutterOG.getDevice().getName());
            isBluetoothConnected = true;
        }

        @Override
        public void onDeviceDisconnected(final BLEError bleError) {
            Log.d(Constants.LOG_TAG, "Disconnected from " + mFlutterOG.getDevice().getName());
            isBluetoothConnected = false;

            // Check for errors
            if (bleError.getType() != BLEError.Type.NO_ERROR) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(mActivity, R.style.AppTheme));
                        adb.setMessage(bleError.getMessage());
                        adb.setTitle("Disconnected");
                        adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flutterConnectListener.onFlutterConnected(isBluetoothConnected);
                            }
                        });
                        AlertDialog dialog = adb.create();
                        dialog.show();
                    }
                });
            } else {
                flutterConnectListener.onFlutterConnected(isBluetoothConnected);
            }

            isBluetoothConnected = false;
            globalHandler.melodySmartDeviceHandler.unregisterListeners(melodySmartListener,dataServiceListener);
        }

        @Override
        public void onOtauAvailable() {
            // I don't think this applies to us
        }

        @Override
        public void onOtauRecovery(DeviceDatabase.DeviceData deviceData) {
            // I don't think this applies to us
        }
    };


    private DataService.Listener dataServiceListener = new DataService.Listener() {
        @Override
        public void onConnected(final boolean isFound) {
            if (isFound) {
                Log.d(Constants.LOG_TAG, "Connected to " + mFlutterOG.getDevice().getName());
                globalHandler.melodySmartDeviceHandler.getDataService().enableNotifications(true);
            }
            flutterConnectListener.onFlutterConnected(isBluetoothConnected);
        }

        @Override
        public void onReceived(final byte[] bytes) {
            flutterMessageListener.onFlutterMessageReceived(new String(bytes));
            if (!messages.isEmpty()) {
                String msg = messages.poll();
                // So even though there is a callback so I do not send messages on top of each other,
                // the flutter still seems to need some time in order to send all of the messages successfully.
                // For example, making an led relationship we need to send three separate messages for each color (rgb)
                // This is why I put a simple sleep to give the flutter some time.
                // Without this, only the last color would be set, blue.
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.LOG_TAG, msg);
                globalHandler.melodySmartDeviceHandler.getDataService().send(msg.getBytes());
            }
        }
    };


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void startSession(Activity activity, FlutterOG flutterOG) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutterOG.getDevice().getName());
        mActivity = activity;
        mFlutterOG = flutterOG;
        globalHandler.melodySmartDeviceHandler.registerListeners(melodySmartListener,dataServiceListener);
        isBluetoothConnected = false;
        messages = new ConcurrentLinkedQueue<>();
        globalHandler.melodySmartDeviceHandler.connectFlutter(mFlutterOG);
    }


    public void addMessage(String msg) {
        messages.add(msg);
    }


    public void addMessages(ArrayList<String> msgs) {
        messages.addAll(msgs);
    }


    public void sendMessages() {
        if (!messages.isEmpty()) {
            String msg = messages.poll();
            Log.d(Constants.LOG_TAG, msg);
            globalHandler.melodySmartDeviceHandler.getDataService().send(msg.getBytes());
        }
    }


    // Getters and Setters


    public FlutterOG getFlutter() { return mFlutterOG; }
    public BluetoothDevice getFlutterDevice() {
        return mFlutterOG.getDevice();
    }
    public String getFlutterName() {
        return mFlutterOG.getName();
    }
    public void setFlutterConnectListener(final FlutterConnectListener flutterConnectListener) {
        this.flutterConnectListener = flutterConnectListener;
    }
    public void setFlutterMessageListener(final FlutterMessageListener flutterMessageListener) {
        this.flutterMessageListener = flutterMessageListener;
    }

}
