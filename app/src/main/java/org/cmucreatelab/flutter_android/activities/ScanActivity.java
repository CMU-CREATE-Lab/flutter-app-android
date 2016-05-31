package org.cmucreatelab.flutter_android.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.adapters.LeDeviceListAdapter;
import org.cmucreatelab.flutter_android.classes.ScanResult;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

public class ScanActivity extends ListActivity {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private LeDeviceListAdapter mLeDeviceAdapter;
    private boolean mScanning;


    // Class methods


    private synchronized void scanForDevice(final boolean isScanning) {
        mScanning = isScanning;
        if (isScanning) {
            mMelodySmartDevice.startLeScan(mLeScanCallBack);
        } else {
            mMelodySmartDevice.stopLeScan(mLeScanCallBack);
        }
    }


    // Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        globalHandler.newInstance(this.getApplicationContext());
        mScanning = false;

        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(this.getApplicationContext());
        mLeDeviceAdapter = new LeDeviceListAdapter(getLayoutInflater());
        setListAdapter(mLeDeviceAdapter);
        scanForDevice(true);
    }


    @Override
    protected void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy");
        mMelodySmartDevice.disconnect();
        mMelodySmartDevice.close(this);
        scanForDevice(false);
        super.onDestroy();
    }


    private final BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceAdapter.addDevice(new ScanResult(device));
                }
            });
        }
    };


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

}