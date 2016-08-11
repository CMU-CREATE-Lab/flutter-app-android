package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.BondingListener;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.FlutterListener;
import org.cmucreatelab.flutter_android.classes.Flutter;
import org.cmucreatelab.flutter_android.classes.Message;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 5/31/2016.
 *
 * Session Handler
 *
 * A class that handles your session (when you select a specific device on ScanActivity)
 * - connect
 * - disconnect
 * - message sending
 *
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    private Activity mActivity;
    private FlutterListener flutterListener;
    private Flutter mFlutter;
    private MelodySmartDevice mMelodySmartDevice;
    private Message mMessage;
    public boolean isBluetoothConnected;


    private BondingListener bondingListener = new BondingListener() {
        @Override
        public void onBondingStarted() {
            // not sure if we need anything here
        }

        @Override
        public void onBondingFinished(boolean b) {
            // not sure if we need anything here
        }
    };


    private MelodySmartListener melodySmartListener = new MelodySmartListener() {
        @Override
        public void onDeviceConnected() {
            Log.d(Constants.LOG_TAG, "Connecting to " + mFlutter.getDevice().getName());
            isBluetoothConnected = true;
        }

        @Override
        public void onDeviceDisconnected(final BLEError bleError) {
            Log.d(Constants.LOG_TAG, "Disconnected from " + mFlutter.getDevice().getName());
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
                                flutterListener.onConnected(isBluetoothConnected);
                            }
                        });
                        AlertDialog dialog = adb.create();
                        dialog.show();
                    }
                });
            } else {
                flutterListener.onConnected(isBluetoothConnected);
            }
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
                Log.d(Constants.LOG_TAG, "Connected to " + mFlutter.getDevice().getName());
                mMelodySmartDevice.getDataService().enableNotifications(true);
            }
            flutterListener.onConnected(isBluetoothConnected);
        }

        @Override
        public void onReceived(final byte[] bytes) {
            mMessage.setOutput(new String(bytes));
            flutterListener.onMessageSent(mMessage.getOutput());
        }
    };


    public void connect() {
        if (!isBluetoothConnected) {
            mMelodySmartDevice.connect(mFlutter.getDevice().getAddress());
        }
    }


    public SessionHandler() {
        // empty
    }


    public void startSession(Activity activity, Flutter flutter) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutter.getDevice().getName());
        globalHandler = GlobalHandler.newInstance(activity.getApplicationContext());
        mActivity = activity;
        mFlutter = flutter;
        mMessage = new Message();
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.registerListener(bondingListener);
        mMelodySmartDevice.registerListener(melodySmartListener);
        mMelodySmartDevice.getDataService().registerListener(dataServiceListener);
        isBluetoothConnected = false;
        connect();
    }


    public void sendMessage() {
        mMelodySmartDevice.getDataService().send(mMessage.getInput().getBytes());
    }


    public void release() {
        mMelodySmartDevice.unregisterListener(melodySmartListener);
        mMelodySmartDevice.unregisterListener(bondingListener);
        mMelodySmartDevice.getDataService().unregisterListener(dataServiceListener);
        mMelodySmartDevice.disconnect();
    }



    // Getters and Setters


    public BluetoothDevice getFlutterDevice() {
        return mFlutter.getDevice();
    }
    public String getFlutterName() {
        return mFlutter.getName();
    }
    public String getMessageOutput() {
        return mMessage.getOutput();
    }
    public String getMessageInput() {
        return mMessage.getInput();
    }
    public void setMessageOutput(String output) {
        mMessage.setOutput(output);
    }
    public void setMessageInput(String input) {
        mMessage.setInput(input);
    }
    public void setFlutterListener(final FlutterListener flutterListener) {
        this.flutterListener = flutterListener;
    }

}
