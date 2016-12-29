package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothAdapter;

import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.classes.Session;

import java.util.ArrayList;

/**
 * Created by mike on 12/27/16.
 *
 *  Handles interfacing with MelodySmart packages
 */
public class MelodySmartDeviceHandler {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private MelodySmartDataListener melodySmartDataListener = null;
    private MelodySmartDeviceListener melodySmartDeviceListener = null;

    public MelodySmartMessageQueue messageQueue;


    private void unregisterListeners() {
        mMelodySmartDevice.unregisterListener(melodySmartDeviceListener);
        mMelodySmartDevice.getDataService().unregisterListener(melodySmartDataListener);
    }


    private void registerListeners() {
        mMelodySmartDevice.registerListener(melodySmartDeviceListener);
        mMelodySmartDevice.getDataService().registerListener(melodySmartDataListener);
    }


    public MelodySmartDeviceHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(globalHandler.appContext);
        messageQueue = new MelodySmartMessageQueue(getDataService());
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


    public boolean isConnected() {
        return melodySmartDataListener.serviceConnected && melodySmartDeviceListener.deviceConnected;
    }


    public void connect(Session session) {
        if (melodySmartDeviceListener != null || melodySmartDataListener != null) {
            unregisterListeners();
        }
        melodySmartDeviceListener = new MelodySmartDeviceListener(session);
        melodySmartDataListener = new MelodySmartDataListener(session,this);

        registerListeners();
        mMelodySmartDevice.connect(session.flutter.getDevice().getAddress());
    }


    public void disconnect() {
        mMelodySmartDevice.disconnect();
    }


    // BLE scanning


    public synchronized void setFlutterScanning(boolean isScanning, final BluetoothAdapter.LeScanCallback leScanCallback) {
        mMelodySmartDevice.stopLeScan(leScanCallback);
        if (isScanning) {
            mMelodySmartDevice.startLeScan(leScanCallback);
        }
    }

}
