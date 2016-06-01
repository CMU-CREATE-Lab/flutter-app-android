package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.DeviceListener;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

public class DeviceActivity extends AppCompatActivity implements DeviceListener {

    private GlobalHandler globalHandler;

    private TextView guidedInput;
    private EditText dataToSend;
    private EditText dataToReceive;


    // Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        globalHandler = GlobalHandler.newInstance(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_toolbar);
        String deviceName = globalHandler.sessionHandler.getBlueToothDevice().getName();
        if (deviceName != null && deviceName.length() > 0)
            toolbar.setTitle(globalHandler.sessionHandler.getBlueToothDevice().getName());
        else
            toolbar.setTitle(R.string.unknown_device);
        setSupportActionBar(toolbar);

        guidedInput = (TextView) findViewById(R.id.guided_input);
        dataToSend = (EditText) findViewById(R.id.data_to_send);
        dataToReceive = (EditText) findViewById(R.id.data_to_receive);
        dataToSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                globalHandler.sessionHandler.setMessageInput(textView.getText().toString());
                globalHandler.sessionHandler.sendMessage();
                textView.setText("");
                return true;
            }
        });

        globalHandler.sessionHandler.setDeviceListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (globalHandler.sessionHandler.isBluetoothConnected) {
            menu.getItem(1).setTitle("Connected");
        } else {
            menu.getItem(1).setTitle("Disconnected");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy - DeviceActivity");
        globalHandler.sessionHandler.release();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        globalHandler.sessionHandler.release();
        super.onBackPressed();  // optional depending on your needs
        finish();
    }


    @Override
    public void onConnected(final boolean connected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataToSend.setEnabled(connected);
            }
        });
        invalidateOptionsMenu();
    }


    @Override
    public void onMessageSent(final String output) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataToReceive.setText("");
                dataToReceive.setText(output);
            }
        });
    }

}
