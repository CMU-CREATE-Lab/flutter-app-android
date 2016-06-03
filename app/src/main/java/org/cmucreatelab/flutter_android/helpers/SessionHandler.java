package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.BondingListener;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.DeviceListener;
import org.cmucreatelab.flutter_android.classes.Device;
import org.cmucreatelab.flutter_android.classes.Message;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 5/31/2016.
 *
 * This class handles your session (when you select a specific device on ScanActivity)
 * - connect
 * - disconnect
 * - message sending
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    private DeviceListener deviceListener;
    private Device mDevice;
    private MelodySmartDevice mMelodySmartDevice;
    private Message mMessage;
    public boolean isBluetoothConnected;


    private BondingListener bondingListener = new BondingListener() {
        @Override
        public void onBondingStarted() {
            // TODO - add a 'please wait' element here
        }

        @Override
        public void onBondingFinished(boolean b) {
            // TODO - remove the 'please wait' element
        }
    };


    private MelodySmartListener melodySmartListener = new MelodySmartListener() {
        @Override
        public void onDeviceConnected() {
            // TODO - we may need to handle more here
            Log.d(Constants.LOG_TAG, "Connecting to " + mDevice.getDevice().getName());
            isBluetoothConnected = true;
        }

        @Override
        public void onDeviceDisconnected(BLEError bleError) {
            // TODO - we may need to handle more here
            Log.d(Constants.LOG_TAG, "Disconnected from " + mDevice.getDevice().getName());
            isBluetoothConnected = false;

            // Check for errors
            if (bleError.getType() != BLEError.Type.NO_ERROR) {
                AlertDialog.Builder adb = new AlertDialog.Builder(globalHandler.appContext);
                adb.setMessage(bleError.getMessage());
                adb.setTitle("Disconnected");
                adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deviceListener.onConnected(isBluetoothConnected);
                    }
                });
                AlertDialog dialog = adb.create();
                dialog.show();
            } else {
                deviceListener.onConnected(isBluetoothConnected);
            }
        }

        @Override
        public void onOtauAvailable() {
            // TODO - Learn what this is
        }

        @Override
        public void onOtauRecovery(DeviceDatabase.DeviceData deviceData) {
            // TODO - Learn what this is
        }
    };


    private DataService.Listener dataServiceListener = new DataService.Listener() {
        @Override
        public void onConnected(final boolean isFound) {
            // TODO - handle something here
            if (isFound) {
                Log.d(Constants.LOG_TAG, "Connected to " + mDevice.getDevice().getName());
                mMelodySmartDevice.getDataService().enableNotifications(true);
            }
            deviceListener.onConnected(isBluetoothConnected);
        }

        @Override
        public void onReceived(final byte[] bytes) {
            mMessage.setOutput(new String(bytes));
            deviceListener.onMessageSent(mMessage.getOutput());
        }
    };


    // TODO - possibly add a connect button
    public void connect() {
        if (!isBluetoothConnected) {
            mMelodySmartDevice.connect(mDevice.getDevice().getAddress());
        }
    }


    public SessionHandler() {
        // empty
    }


    public void startSession(Device device) {
        Log.d(Constants.LOG_TAG, "Starting session with " + device.getDevice().getName());
        mDevice = device;
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


    public BluetoothDevice getBlueToothDevice() {
        return mDevice.getDevice();
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
    public void setDeviceListener(final DeviceListener deviceListener) {
        this.deviceListener = deviceListener;
    }

}
