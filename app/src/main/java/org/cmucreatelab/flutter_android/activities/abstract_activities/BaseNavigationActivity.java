package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ListView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.activities.TutorialsActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.FlutterStatusDialog;

import java.util.Map;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Steve on 8/22/2016.
 *
 * BaseNavigationActivity
 *
 * An abstract activity used for the basic navigation bar on the bottom of most activities.
 *
 */
public abstract class BaseNavigationActivity extends AppCompatActivity {

    private RobotActivity instance;
    private Session session;
    public DrawerLayout drawerLayout;
    public ListView drawerList;
    public String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    private Map map;

    @OnClick(R.id.drawer_menu_button)
    public void onClickMenuNavButton()
    {
        drawerLayout.openDrawer(Gravity.START);
    }

    @OnClick(R.id.nav_tutorials)
    public void onClickTutorialsButton()
    {
        Log.d(Constants.LOG_TAG, "onClickTutorialsButton");
        Intent intent = new Intent(this, TutorialsActivity.class);
        startActivity(intent);
    }

    protected void onCreateDrawer()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, 0, 0);
        drawerLayout.setDrawerListener(drawerToggle);

//        View header = getLayoutInflater().inflate(R.layout.activity_robot, null);
//        header.findViewById(R.id.drawer_layout);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

/*        layers = getResources().getStringArray(R.array.layers_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        View header = getLayoutInflater().inflate(R.layout.drawer_list_header, null);
        drawerList.addHeaderView(header, null, false);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, android.R.id.text1,
                layers));
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.drawer_list_footer, null, false);
        drawerList.addFooterView(footerView);

        drawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                map.drawerClickEvent(pos);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "onCreate - " + getClass() );
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "onResume - " + getClass() );
        GlobalHandler.getInstance(getApplicationContext()).sessionHandler.setCurrentActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.LOG_TAG, "onPause - " + getClass() );
        Session session = GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession();
        if (session != null && session.isSimulatingData()) {
            Log.v(Constants.LOG_TAG, "stop simulating data (onPause)");
            GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession().setSimulatingData(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, "onDestroy - " + getClass() );
    }


    @Optional @OnClick(R.id.text_menu_sensor)
    public void onClickSensorsMenu() {
        Log.d(Constants.LOG_TAG, "onClickSensors");
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }


    @Optional @OnClick(R.id.text_menu_robot)
    public void onClickRobotMenu() {
        Log.d(Constants.LOG_TAG, "onClickRobotMenu");
        Intent intent = new Intent(this, RobotActivity.class);
        startActivity(intent);
    }


    @Optional @OnClick(R.id.text_menu_datalog)
    public void onClickDataLogMenu() {
        Log.d(Constants.LOG_TAG, "onClickDataLogMenu");
        Intent intent = new Intent(this, DataLogsActivity.class);
        startActivity(intent);
    }

    @Optional @OnClick(R.id.image_flutter_status_button)
    public void onClickFlutterStatus() {
        Log.d(Constants.LOG_TAG, "onClickFlutterStatus");
        FlutterStatusDialog.displayDialog(this, 0);
    }

}
