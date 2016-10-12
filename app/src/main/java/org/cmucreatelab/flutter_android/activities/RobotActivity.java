package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;

import butterknife.ButterKnife;

public class RobotActivity extends BaseNavigationActivity {

    private GlobalHandler globalHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_robot));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        globalHandler = GlobalHandler.newInstance(this);
        if (!globalHandler.sessionHandler.isBluetoothConnected) {
            NoFlutterConnectedDialog noFlutterConnectedDialog = NoFlutterConnectedDialog.newInstance(R.string.no_flutter_robot);
            noFlutterConnectedDialog.setCancelable(false);
            noFlutterConnectedDialog.show(getSupportFragmentManager(), "tag");
        }
    }
}
