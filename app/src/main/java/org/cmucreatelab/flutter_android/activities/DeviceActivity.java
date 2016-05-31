package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.bluecreation.melodysmart.BLEError;
import com.bluecreation.melodysmart.BondingListener;
import com.bluecreation.melodysmart.DeviceDatabase;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

public class DeviceActivity extends AppCompatActivity {

    private GlobalHandler globalHandler;
    private MelodySmartDevice mMelodySmartDevice;
    private boolean isConnected;


    // Listeners


    private MelodySmartListener melodySmartListener = new MelodySmartListener() {
        @Override
        public void onDeviceConnected() {
            // TODO - handle connection status
            Log.d(Constants.LOG_TAG, "Connected to " + globalHandler.sessionHandler.getBlueToothDevice().getName());
            invalidateOptionsMenu();
            isConnected = true;
        }

        @Override
        public void onDeviceDisconnected(BLEError bleError) {
            // TODO - handle disconnect
            Log.d(Constants.LOG_TAG, "Disconnected from " + globalHandler.sessionHandler.getBlueToothDevice().getName());
            invalidateOptionsMenu();
            isConnected = false;
            finish();
        }

        @Override
        public void onOtauAvailable() {

        }

        @Override
        public void onOtauRecovery(DeviceDatabase.DeviceData deviceData) {

        }
    };


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        globalHandler = GlobalHandler.newInstance(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_toolbar);
        toolbar.setTitle(globalHandler.sessionHandler.getBlueToothDevice().getName());
        setSupportActionBar(toolbar);

        isConnected = false;
        mMelodySmartDevice = MelodySmartDevice.getInstance();
        mMelodySmartDevice.registerListener(bondingListener);
        mMelodySmartDevice.registerListener(melodySmartListener);
        mMelodySmartDevice.connect(globalHandler.sessionHandler.getBlueToothDevice().getAddress());
    }


    @Override
    protected void onResume() {
        Log.d(Constants.LOG_TAG, "onResume - DeviceActivity");
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.device_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isConnected) {
            menu.getItem(1).setTitle("Connected");
        } else {
            menu.getItem(1).setTitle("Disconnected");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy - DeviceActivity");
        mMelodySmartDevice.unregisterListener(melodySmartListener);
        mMelodySmartDevice.unregisterListener(bondingListener);
        mMelodySmartDevice.disconnect();
        super.onDestroy();
    }

}
