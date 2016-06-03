package org.cmucreatelab.flutter_android.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.adapters.LeDeviceListAdapter;
import org.cmucreatelab.flutter_android.classes.Device;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private LeDeviceListAdapter mLeDeviceAdapter;
    private ArrayList<Device> mDevices;
    private boolean mScanning;


    // Class methods


    private void clearAll() {
        mDevices.clear();
        mLeDeviceAdapter.clearDevices();
    }


    private synchronized void scanForDevice(final boolean isScanning) {
        mScanning = isScanning;
        if (isScanning) {
            clearAll();
            mMelodySmartDevice.startLeScan(mLeScanCallBack);
        } else {
            mMelodySmartDevice.stopLeScan(mLeScanCallBack);
        }
        invalidateOptionsMenu();
    }


    // Listeners


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
                    // Check if the device is a flutter or not
                    String address = device.getAddress();
                    address = address.substring(0,8);
                    if (address.equals(Constants.FLUTTER_MAC_ADDRESS)) {
                        Device endResult = new Device(device);
                        mDevices.add(endResult);
                        mLeDeviceAdapter.addDevice(endResult);
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scan_toolbar);
        setSupportActionBar(toolbar);
        globalHandler = GlobalHandler.newInstance(this.getApplicationContext());

        mDevices = new ArrayList<>();

        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(this.getApplicationContext());
        mLeDeviceAdapter = new LeDeviceListAdapter(getLayoutInflater());
        ListView list = (ListView) findViewById(R.id.scan_list);
        list.setAdapter(mLeDeviceAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                scanForDevice(false);
                globalHandler.sessionHandler.startSession(mDevices.get(i));
                Intent intent = new Intent(getApplicationContext(), DeviceActivity.class);
                startActivity(intent);
            }
        });

        // Just in case someone got a hold of the app without BLE support
        PackageManager pm = getApplicationContext().getPackageManager();
        boolean isSupported = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupported) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setMessage(R.string.ble_unsupported);
            adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog dialog = adb.create();
            dialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.progress_bar);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                clearAll();
                scanForDevice(true);
                break;

            case R.id.menu_stop:
                scanForDevice(false);
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        Log.d(Constants.LOG_TAG, "onResume - ScanActivity");
        clearAll();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy - ScanActivity");
        scanForDevice(false);
        mMelodySmartDevice.close(this);
        super.onDestroy();
    }

}