package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mike on 12/27/16.
 *
 *  Handles interfacing with MelodySmart packages
 */
public class MelodySmartDeviceHandler {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice; // used for connecting/disconnecting to a device and sending messages to the bluetooth device and back
    protected ConcurrentLinkedQueue<String> messages;
    public MelodySmartDeviceListener melodySmartDeviceListener = null;
    private MelodySmartDataListener melodySmartDataListener = null;


    public MelodySmartDeviceHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(globalHandler.appContext);
        messages = new ConcurrentLinkedQueue<>();
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


    private void unregisterListeners() {
        mMelodySmartDevice.unregisterListener(melodySmartDeviceListener);
        mMelodySmartDevice.getDataService().unregisterListener(melodySmartDataListener);
    }


    private void registerListeners() {
        mMelodySmartDevice.registerListener(melodySmartDeviceListener);
        mMelodySmartDevice.getDataService().registerListener(melodySmartDataListener);
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


    public synchronized void setFlutterScanning(boolean isScanning, final BluetoothAdapter.LeScanCallback leScanCallback) {
        mMelodySmartDevice.stopLeScan(leScanCallback);
        if (isScanning) {
            mMelodySmartDevice.startLeScan(leScanCallback);
        }
    }


    public DataService getDataService() {
        return mMelodySmartDevice.getDataService();
    }

}
