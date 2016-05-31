package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.BondingListener;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

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

    private DeviceListener deviceListener;
    private Device mDevice;
    private MelodySmartDevice mMelodySmartDevice;
    private Message mMessage;
    public boolean isBluetoothConnected;


    private BondingListener bondingListener = new BondingListener() {
        @Override
        public void onBondingStarted() {
            // TODO - add some sort of please wait element here
        }

        @Override
        public void onBondingFinished(boolean b) {
            // TODO - remove the please wait element
        }
    };


    private MelodySmartListener melodySmartListener = new MelodySmartListener() {
        @Override
        public void onDeviceConnected() {
            // TODO - handle connection status
            Log.d(Constants.LOG_TAG, "Connected to " + mDevice.getDevice().getName());
            isBluetoothConnected = true;
            deviceListener.onConnected(isBluetoothConnected);
        }

        @Override
        public void onDeviceDisconnected(BLEError bleError) {
            // TODO - handle disconnect
            Log.d(Constants.LOG_TAG, "Disconnected from " + mDevice.getDevice().getName());
            isBluetoothConnected = false;
            deviceListener.onConnected(isBluetoothConnected);
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
                mMelodySmartDevice.getDataService().enableNotifications(true);
            }
        }

        @Override
        public void onReceived(final byte[] bytes) {
            mMessage.setOutput(new String(bytes));
            deviceListener.onMessageSent(mMessage.getOutput());
        }
    };


    public SessionHandler() {
        mDevice = null;
        mMessage = new Message();
    }


    public void startSession(Device device) {
        Log.d(Constants.LOG_TAG, "Starting session with " + device.getDevice().getName());
        mDevice = device;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.registerListener(bondingListener);
        mMelodySmartDevice.registerListener(melodySmartListener);
        mMelodySmartDevice.getDataService().registerListener(dataServiceListener);
        mMelodySmartDevice.connect(mDevice.getDevice().getAddress());
        isBluetoothConnected = false;
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
