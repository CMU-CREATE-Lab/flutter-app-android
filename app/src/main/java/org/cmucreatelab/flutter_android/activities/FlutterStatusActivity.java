package org.cmucreatelab.flutter_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pdille on 2/20/2017.
 *
 * FlutterStatusActivity
 *
 * An activity that shows what Flutter is paired with the app and gives the user the ability to disconnect.
 */
public class FlutterStatusActivity extends BaseNavigationActivity {

    public static final String FLUTTER_STATUS_ACTIVITY_KEY = "flutter_status_activity_key";

    private GlobalHandler globalHandler;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_status);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(getApplicationContext());
        this.session = globalHandler.sessionHandler.getSession();

        // construct toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g));
        setSupportActionBar(toolbar);

        TextView flutterStatusName = (TextView) findViewById(R.id.text_flutter_status_name);
        TextView flutterStatusText = (TextView) findViewById(R.id.text_flutter_status);
        ImageView flutterStatusIcon = (ImageView) findViewById(R.id.image_flutter_status_pic);
        Button flutterConnectDisconnect = (Button) findViewById(R.id.button_flutter_connect_disconnect);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
            flutterConnectDisconnect.setBackgroundResource(R.drawable.round_green_button);
            flutterConnectDisconnect.setText(R.string.connect_flutter);
        } else {
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            String flutterName = session.getFlutter().getName();
            flutterStatusName.setText(flutterName);
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);
            flutterConnectDisconnect.setBackgroundResource(R.drawable.round_reddish_button);
            flutterConnectDisconnect.setText(R.string.disconnect);
        }
    }

    @OnClick(R.id.button_flutter_connect_disconnect)
    public void onClickConnectDisconnect() {
        Log.d(Constants.LOG_TAG, "onClickConnectDisconnect");
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            globalHandler.melodySmartDeviceHandler.disconnect(false);
        } else {
            Intent intent = new Intent(this, AppLandingActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        super.onBackPressed();
        finish();
    }

}
