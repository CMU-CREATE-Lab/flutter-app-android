package org.cmucreatelab.flutter_android.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.DeviceListener;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.GuidedInputHandler;
import org.cmucreatelab.flutter_android.helpers.guided_input.Node;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

public class DeviceActivity extends AppCompatActivity implements DeviceListener {

    private Activity thisActivity;
    private GlobalHandler globalHandler;

    private TextView promptTitle;
    private LinearLayout guidedInputContainer;
    private EditText dataToSend;
    private EditText dataToReceive;


    // Listeners


    private final TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            globalHandler.sessionHandler.setMessageInput(textView.getText().toString());
            globalHandler.sessionHandler.sendMessage();
            textView.setText("");
            // TODO - do we want the keyboard to disappear when a message has been sent?
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            return true;
        }
    };


    private final TextWatcher textWatcher = new TextWatcher() {

        private boolean isValid;
        private int previousSize;
        private int badEntryCounter = 0;


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            previousSize = charSequence.length();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String temp = "";
            if (charSequence.length() > 0) {
                temp = charSequence.toString().substring(charSequence.length()-1, charSequence.length());
            }

            Log.d(Constants.LOG_TAG, String.valueOf(previousSize));
            Log.d(Constants.LOG_TAG, String.valueOf(charSequence.length()));

            if (previousSize < charSequence.length()) {
                isValid = globalHandler.guidedInputHandler.choosePrompt(thisActivity, temp, guidedInputContainer, promptTitle);
            } else {
                Log.d(Constants.LOG_TAG, String.valueOf(badEntryCounter));
                if (badEntryCounter == 0) {
                    isValid = globalHandler.guidedInputHandler.choosePrompt(thisActivity, GuidedInputHandler.PARENT_PROMPT, guidedInputContainer, promptTitle);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!isValid) {
                if(previousSize > editable.length()) {
                    if (badEntryCounter > 0) {
                        badEntryCounter--;
                    }
                } else {
                    badEntryCounter++;
                }
            } else {
                badEntryCounter = 0;
            }
        }
    };


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
        thisActivity = this;

        promptTitle = (TextView) findViewById(R.id.prompt_title);
        guidedInputContainer = (LinearLayout) findViewById(R.id.guided_input_container);
        dataToSend = (EditText) findViewById(R.id.data_to_send);
        dataToReceive = (EditText) findViewById(R.id.data_to_receive);
        dataToSend.setOnEditorActionListener(onEditorActionListener);
        dataToSend.addTextChangedListener(textWatcher);
        globalHandler.sessionHandler.setDeviceListener(this);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(5, Color.BLACK);
        guidedInputContainer.setBackground(drawable);

        // initialize the prompt
        globalHandler.guidedInputHandler.choosePrompt(this, GuidedInputHandler.PARENT_PROMPT, guidedInputContainer, promptTitle);
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

}
