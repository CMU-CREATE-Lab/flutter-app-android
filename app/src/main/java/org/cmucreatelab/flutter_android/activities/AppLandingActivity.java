package org.cmucreatelab.flutter_android.activities;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.LeDeviceListAdapter;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.NamingHandler;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 5/26/2016.
 *
 * AppLandingActivity
 *
 * An activity that can scan for flutters nearby and connect to them.
 *
 */
public class AppLandingActivity extends BaseNavigationActivity implements FlutterConnectListener,
        DataLoggingHandler.DataSetPointsListener{

    private LeDeviceListAdapter mLeDeviceAdapter;

    // TODO @tasota we could move this to its own class and have MelodySamrtDeviceHandler contain the instance
    private final BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0; i<mLeDeviceAdapter.getCount(); i++) {
                        FlutterOG result = (FlutterOG)mLeDeviceAdapter.getItem(i);
                        if (result.getDevice().equals(device)) {
                            return;
                        }
                    }
                    // Check if the device is a flutter or not
                    String address = device.getAddress();
                    address = address.substring(0,8);
                    if (address.equals(Constants.FLUTTER_MAC_ADDRESS)) {
                        findViewById(R.id.image_flutter).setVisibility(View.GONE);
                        String name = NamingHandler.generateName(getApplicationContext(),device.getAddress());
                        FlutterOG endResult = new FlutterOG(device, name);
                        mLeDeviceAdapter.addDevice(endResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.frame_second_scan).setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    };


    // Class methods


    private void scanForDevice(boolean isScanning) {
        Button scan = (Button) findViewById(R.id.button_scan);
        ListView list = (ListView) findViewById(R.id.scan_list);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        globalHandler.melodySmartDeviceHandler.setFlutterScanning(isScanning, mLeScanCallBack);
        if (isScanning) {
            scan.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_white));
            scan.setText(R.string.scanning);
            scan.setTextColor(Color.BLACK);
            list.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.image_timed_prompt).setVisibility(View.INVISIBLE);
            findViewById(R.id.frame_second_scan).setVisibility(View.GONE);
            findViewById(R.id.image_flutter).setVisibility(View.VISIBLE);
            scan.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button));
            scan.setText(R.string.scan);
            scan.setTextColor(Color.WHITE);
            list.setVisibility(View.GONE);
        }

        mLeDeviceAdapter.clearDevices();
    }


    private void showAlertBleUnsupported() {
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


    private void showAlertBluetoothDisabled(final BluetoothAdapter bluetoothAdapter) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage(R.string.enable_bluetooth_msg);
        adb.setTitle(R.string.enable_bluetooth);
        adb.setPositiveButton(R.string.positive_response, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // nothing because we are overriding it
            }
        });

        final AlertDialog dialog = adb.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.LOG_TAG, String.valueOf(bluetoothAdapter.isEnabled()));
                if (bluetoothAdapter.isEnabled()) {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_landing);
        ButterKnife.bind(this);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        // Just in case someone got a hold of the app without BLE support
        PackageManager pm = getApplicationContext().getPackageManager();
        boolean isSupported = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupported) {
            showAlertBleUnsupported();
        } else {
            // TODO @tasota do we want this to keep popping up in this fashion?
            final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                showAlertBluetoothDisabled(bluetoothAdapter);
            }

            // construct toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
            toolbar.setContentInsetsAbsolute(0,0);
            toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g));
            setSupportActionBar(toolbar);

            // setup adapter for LeScan
            mLeDeviceAdapter = new LeDeviceListAdapter(getLayoutInflater());
            ListView list = (ListView) findViewById(R.id.scan_list);
            list.setAdapter(mLeDeviceAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FlutterOG flutter = (FlutterOG)mLeDeviceAdapter.getItem(i);
                    scanForDevice(false);
                    globalHandler.sessionHandler.startSession(AppLandingActivity.this, flutter);
                }
            });

            // TODO debugging screen size (can remove later)
            DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;
            String toastMsg;
            switch(screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    toastMsg = "Large screen";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    toastMsg = "Normal screen";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    toastMsg = "Small screen";
                    break;
                default:
                    toastMsg = "Screen size is neither large, normal or small";
            }
            toastMsg += " (dim="+String.valueOf(dpHeight)+"x"+String.valueOf(dpWidth)+")";
            Log.v(Constants.LOG_TAG, toastMsg);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        scanForDevice(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanForDevice(false);
    }


    @OnClick(R.id.button_scan)
    public void onClickScan() {
        Log.d(Constants.LOG_TAG, "onClickScan");
        scanForDevice(true);
    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        Log.d(Constants.LOG_TAG, "AppLanding.onDataSetPointsPopulated - Success: " + isSuccess);

        // dismiss spinner
        GlobalHandler.getInstance(this).sessionHandler.dismissProgressDialog();

        // start new activity
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }


    // FlutterConnectListener implementation


    @Override
    public void onFlutterConnected() {
        Log.d(Constants.LOG_TAG, "AppLandingActivity.onFlutterConnected");
        final GlobalHandler globalHandler = GlobalHandler.getInstance(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalHandler.sessionHandler.updateProgressDialogMessage("Reading Flutter Data...");
            }
        });
        globalHandler.dataLoggingHandler.populatePointsAvailable(this);
    }


    @Override
    public void onFlutterDisconnected() {
        Log.d(Constants.LOG_TAG, "AppLandingActivity.onFlutterDisconnected");
        Intent intent = new Intent(this, AppLandingActivity.class);
        startActivity(intent);
    }

}