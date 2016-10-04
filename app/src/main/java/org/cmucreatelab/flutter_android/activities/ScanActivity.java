package org.cmucreatelab.flutter_android.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bluecreation.melodysmart.MelodySmartDevice;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.LeDeviceListAdapter;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by Steve on 5/26/2016.
 *
 * ScanActivity
 *
 * An activity that can scan for flutters nearby.
 *
 */
public class ScanActivity extends BaseNavigationActivity implements FlutterConnectListener {

    private MelodySmartDevice mMelodySmartDevice;
    private LeDeviceListAdapter mLeDeviceAdapter;
    private ArrayList<FlutterOG> mFlutterOGs;
    private boolean mScanning;


    private Timer timer;
    private static int FIRST_SCAN_ID;
    private static int SECOND_SCAN_ID;


    // Class methods


    private void startTimer(final int ms) {
        timer.cancel();
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.image_timed_prompt).setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        timer.schedule(timerTask, ms);
    }


    private void clearAll() {
        mFlutterOGs.clear();
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
    }


    // Listeners


    private final BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (FlutterOG result : mFlutterOGs) {
                        if (result.getDevice().equals(device)) {
                            return;
                        }
                    }
                    // Check if the device is a flutter or not
                    String address = device.getAddress();
                    address = address.substring(0,8);
                    if (address.equals(Constants.FLUTTER_MAC_ADDRESS)) {
                        String name = globalHandler.namingHandler.generateName(device.getAddress());
                        FlutterOG endResult = new FlutterOG(device, name);
                        mFlutterOGs.add(endResult);
                        mLeDeviceAdapter.addDevice(endResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.cancel();
                                findViewById(FIRST_SCAN_ID).setVisibility(View.GONE);
                                findViewById(SECOND_SCAN_ID).setVisibility(View.VISIBLE);
                                startTimer(7500);
                            }
                        });
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolbar);
        globalHandler = GlobalHandler.newInstance(this.getApplicationContext());
        globalHandler.sessionHandler.setFlutterConnectListener(this);
        final Activity activity = this;

        mFlutterOGs = new ArrayList<>();
        timer = new Timer();

        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.init(this.getApplicationContext());
        mLeDeviceAdapter = new LeDeviceListAdapter(getLayoutInflater());
        ListView list = (ListView) findViewById(R.id.scan_list);
        list.setAdapter(mLeDeviceAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timer.cancel();
                scanForDevice(false);
                globalHandler.sessionHandler.startSession(activity, mFlutterOGs.get(i));
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
    protected void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "onResume - ScanActivity");
        FIRST_SCAN_ID = R.id.frame_first_scan;
        SECOND_SCAN_ID = R.id.frame_second_scan;
        clearAll();
        findViewById(R.id.image_timed_prompt).setVisibility(View.INVISIBLE);
        findViewById(FIRST_SCAN_ID).setVisibility(View.VISIBLE);
        findViewById(SECOND_SCAN_ID).setVisibility(View.GONE);
        scanForDevice(true);
        startTimer(7500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, "onDestroy - ScanActivity");
        scanForDevice(false);
    }


    @Override
    public void onConnected(boolean connected) {
        Log.d(Constants.LOG_TAG, "Is connected: " + connected);
        if (connected) {
            Intent intent = new Intent(this, SensorsActivity.class);
            startActivity(intent);
        }
    }

}