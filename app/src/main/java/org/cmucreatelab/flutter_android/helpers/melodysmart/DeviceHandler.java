package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.bluetooth.BluetoothAdapter;

import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.melodysmart.listeners.DataListener;
import org.cmucreatelab.flutter_android.helpers.melodysmart.listeners.DeviceListener;

import java.util.ArrayList;

/**
 * Created by mike on 12/27/16.
 *
 * Handles interfacing with MelodySmart packages
 *
 */
public class DeviceHandler {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private DataListener dataListener = null;
    private DeviceListener deviceListener = null;
    private MessageQueue messageQueue;


    private void unregisterListeners() {
        mMelodySmartDevice.unregisterListener(deviceListener);
        mMelodySmartDevice.getDataService().unregisterListener(dataListener);
    }


    private void registerListeners() {
        mMelodySmartDevice.registerListener(deviceListener);
        mMelodySmartDevice.getDataService().registerListener(dataListener);
    }


    public DeviceHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(globalHandler.appContext);
        messageQueue = new MessageQueue(getDataService());
    }


    // message-sending


    public DataService getDataService() {
        return mMelodySmartDevice.getDataService();
    }


    public void addMessage(String msg) {
        messageQueue.addMessage(msg);
    }


    public void addMessages(ArrayList<String> msgs) {
        messageQueue.addMessages(msgs);
    }


    // connect/disconnect for a session


    /**
     * Determine if a MelodySmart device is currently connected.
     *
     * @return true if both the DataService listener and MelodySmart listeners exist and are still registered/connected.
     */
    public boolean isConnected() {
        if (dataListener == null || deviceListener == null)
            return false;
        return dataListener.isServiceConnected() && deviceListener.isDeviceConnected();
    }


    /**
     * Request to connect a MelodySmart device.
     *
     * @param session: used for listeners and to grab the Flutter's address.
     */
    public void connect(Session session) {
        if (deviceListener != null || dataListener != null) {
            unregisterListeners();
        }
        deviceListener = new DeviceListener(session);
        dataListener = new DataListener(session,this);

        registerListeners();
        mMelodySmartDevice.connect(session.getFlutter().getDevice().getAddress());
    }


    /**
     * Request to disconnect from all MelodySmart devices.
     */
    public void disconnect() {
        mMelodySmartDevice.disconnect();
    }


    // BLE scanning


    /**
     * Toggles MelodySmart device scanning (i.e. scanning for Flutter devices).
     *
     * @param isScanning: true indicates that the LeScan should be started/restarted.
     * @param leScanCallback: A callback for when a bluetooth device is scanned.
     */
    public synchronized void setFlutterScanning(boolean isScanning, final BluetoothAdapter.LeScanCallback leScanCallback) {
        mMelodySmartDevice.stopLeScan(leScanCallback);
        if (isScanning) {
            mMelodySmartDevice.startLeScan(leScanCallback);
        }
    }

}
