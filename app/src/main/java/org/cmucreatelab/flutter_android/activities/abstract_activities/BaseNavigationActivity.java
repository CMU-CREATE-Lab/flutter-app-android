package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.GlossaryActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.activities.TutorialsActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.FlutterStatusDialog;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Steve on 8/22/2016.
 * <p>
 * BaseNavigationActivity
 * <p>
 * An abstract activity used for the basic navigation bar on the bottom of most activities.
 */
public abstract class BaseNavigationActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    private void onCreateDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, 0, 0);

        try {
            drawerLayout.setDrawerListener(drawerToggle);
        }
        catch (NullPointerException npe)
        {
            Log.i("Small Screen Device", "Can't instantiate drawer");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            drawerToggle.syncState();
        }
        catch (NullPointerException npe)
        {
            Log.i("Small Screen Device", "Can't instantiate drawer");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "onCreate - " + getClass());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "onResume - " + getClass());
        GlobalHandler.getInstance(getApplicationContext()).sessionHandler.setCurrentActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.LOG_TAG, "onPause - " + getClass());
        Session session = GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession();
        if (session != null && session.isSimulatingData()) {
            Log.v(Constants.LOG_TAG, "stop simulating data (onPause)");
            GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession().setSimulatingData(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, "onDestroy - " + getClass());
    }


    @Optional
    @OnClick(R.id.text_menu_sensor)
    public void onClickSensorsMenu() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }


    @Optional
    @OnClick(R.id.text_menu_robot)
    public void onClickRobotMenu() {
        Log.d(Constants.LOG_TAG, "onClickRobotMenu");
        Intent intent = new Intent(this, RobotActivity.class);
        startActivity(intent);
    }


    @Optional
    @OnClick(R.id.text_menu_datalog)
    public void onClickDataLogMenu() {
        Log.d(Constants.LOG_TAG, "onClickDataLogMenu");
        Intent intent = new Intent(this, DataLogsActivity.class);
        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.image_flutter_status_button)
    public void onClickFlutterStatus() {
        Log.d(Constants.LOG_TAG, "onClickFlutterStatus");
        FlutterStatusDialog.displayDialog(this, 0);
    }

    @Optional
    @OnClick(R.id.drawer_menu_button)
    public void onClickMenuNavButton() {
        drawerLayout.openDrawer(Gravity.START);
    }

    @Optional
    @OnClick(R.id.button_close_drawer)
    public void onClickCloseDrawerButton() {
        drawerLayout.closeDrawer(Gravity.START);
    }

    @Optional
    @OnClick(R.id.nav_tutorials)
    public void onClickTutorialsButton() {
        Log.d(Constants.LOG_TAG, "onClickTutorialsButton");
        Intent intent = new Intent(this, TutorialsActivity.class);
        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.nav_glossary)
    public void onClickGlossaryButton() {
        Log.d(Constants.LOG_TAG, "onClickGlossaryButton");
        Intent intent = new Intent(this, GlossaryActivity.class);
        startActivity(intent);
    }
}
