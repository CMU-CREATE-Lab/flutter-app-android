package org.cmucreatelab.flutter_android.activities;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.adapters.LeDeviceListAdapter;
import org.cmucreatelab.flutter_android.classes.Device;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

public class ScanActivity extends ListActivity {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private LeDeviceListAdapter mLeDeviceAdapter;
    private ArrayList<Device> mDevices;
    private boolean mScanning;


    // Class methods


    private synchronized void scanForDevice(final boolean isScanning) {
        mScanning = isScanning;
        if (isScanning) {
            findViewById(R.id.progress_scanning).setVisibility(View.VISIBLE);
            mMelodySmartDevice.startLeScan(mLeScanCallBack);
        } else {
            findViewById(R.id.progress_scanning).setVisibility(View.INVISIBLE);
            mMelodySmartDevice.stopLeScan(mLeScanCallBack);
        }
    }


    // Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }


    @Override
    protected void onResume() {
        Log.d(Constants.LOG_TAG, "onResume - ScanActivity");
        globalHandler = GlobalHandler.newInstance(this.getApplicationContext());
        mScanning = false;
        mDevices = new ArrayList<>();

        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(this.getApplicationContext());
        mLeDeviceAdapter = new LeDeviceListAdapter(getLayoutInflater());
        setListAdapter(mLeDeviceAdapter);
        scanForDevice(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy - ScanActivity");
        scanForDevice(false);
        super.onDestroy();
    }


    private final BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Device result : mDevices) {
                        if (result.getDevice().equals(device)) {
                            return;
                        }
                    }
                    Device endResult = new Device(device);
                    mDevices.add(endResult);
                    mLeDeviceAdapter.addDevice(endResult);
                }
            });
        }
    };


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        scanForDevice(false);
        globalHandler.sessionHandler.startSession(mDevices.get(position));
        Intent intent = new Intent(getApplicationContext(), DeviceActivity.class);
        startActivity(intent);
        finish();
    }

}