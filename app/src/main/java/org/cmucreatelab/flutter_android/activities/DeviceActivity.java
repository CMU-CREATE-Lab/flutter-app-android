package org.cmucreatelab.flutter_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.DeviceListener;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.GuidedInputHandler;
import org.cmucreatelab.flutter_android.helpers.GuidedInputStates;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceActivity extends AppCompatActivity implements DeviceListener {

    private Activity thisActivity;
    private GlobalHandler globalHandler;
    private GuidedInputHandler guidedInputHandler;

    private TextView promptTitle;
    private LinearLayout guidedInputContainer;
    private EditText dataToSend;
    private EditText dataToReceive;


    private void fromBeginning() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(1);
        dataToSend.setText(null);
        guidedInputContainer.setVisibility(View.VISIBLE);
        dataToSend.setFilters(filters);
        guidedInputHandler = new GuidedInputHandler(promptTitle, guidedInputContainer);
        guidedInputHandler.choosePrompt(this, null);
    }


    // Listeners


    private final TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (globalHandler.appState.currentState != GuidedInputStates.READY_TO_SEND) {
                Editable editable = dataToSend.getText();
                guidedInputHandler.choosePrompt(thisActivity, editable);
                editable.clear();
            } else {
                globalHandler.sessionHandler.setMessageInput(guidedInputHandler.getFinalString());
                globalHandler.sessionHandler.sendMessage();
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        globalHandler = GlobalHandler.newInstance(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_toolbar);
        String deviceName = globalHandler.sessionHandler.getName();
        if (deviceName != null && deviceName.length() > 0)
            toolbar.setTitle(deviceName);
        else
            toolbar.setTitle(R.string.unknown_device);
        setSupportActionBar(toolbar);
        thisActivity = this;

        promptTitle = (TextView) findViewById(R.id.prompt_title);
        guidedInputContainer = (LinearLayout) findViewById(R.id.guided_input_container);
        dataToSend = (EditText) findViewById(R.id.data_to_send);
        dataToReceive = (EditText) findViewById(R.id.data_to_receive);
        dataToSend.setOnEditorActionListener(onEditorActionListener);
        globalHandler.sessionHandler.setDeviceListener(this);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(5, Color.BLACK);
        guidedInputContainer.setBackground(drawable);

        fromBeginning();
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
        globalHandler.sessionHandler.release();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        globalHandler.sessionHandler.release();
        super.onBackPressed();
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


    @OnClick(R.id.restart_input)
    public void onRestartInput() {
        fromBeginning();
    }

}
