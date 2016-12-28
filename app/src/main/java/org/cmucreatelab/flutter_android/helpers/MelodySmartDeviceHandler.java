package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothAdapter;

import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;

/**
 * Created by mike on 12/27/16.
 *
 *  Handles interfacing with MelodySmart packages
 */
public class MelodySmartDeviceHandler {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice; // used for connecting/disconnecting to a device and sending messages to the bluetooth device and back


    public MelodySmartDeviceHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(globalHandler.appContext);
    }


    public void connectFlutter(FlutterOG flutter) {
        if (!globalHandler.sessionHandler.isBluetoothConnected)
            mMelodySmartDevice.connect(flutter.getDevice().getAddress());
    }


    public void disconnectFlutter() {
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


    public void registerListeners(MelodySmartListener melodySmartListener, DataService.Listener dataServiceListener) {
        mMelodySmartDevice.registerListener(melodySmartListener);
        mMelodySmartDevice.getDataService().registerListener(dataServiceListener);
    }


    public void unregisterListeners(MelodySmartListener melodySmartListener, DataService.Listener dataServiceListener) {
        mMelodySmartDevice.unregisterListener(melodySmartListener);
        mMelodySmartDevice.getDataService().unregisterListener(dataServiceListener);
    }

}
