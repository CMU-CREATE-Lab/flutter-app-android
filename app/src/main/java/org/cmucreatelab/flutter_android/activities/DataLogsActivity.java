package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;

public class DataLogsActivity extends BaseNavigationActivity {


    private void onClickGetLog() {
        Log.d(Constants.LOG_TAG, "onClickGetLog");
    }


    private void onClickOpenLog() {
        Log.d(Constants.LOG_TAG, "onClickOpenLog");
    }


    private void onClickSendLog() {
        Log.d(Constants.LOG_TAG, "onClickSendLog");
    }


    private void onClickCleanUpLogs() {
        Log.d(Constants.LOG_TAG, "onClickCleanUpLogs");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logs);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_data_logs, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_get_log:
                onClickGetLog();
                return true;
            case R.id.item_open_log:
                onClickOpenLog();
                return true;
            case R.id.item_send_log:
                onClickSendLog();
                return true;
            case R.id.item_clean_up_logs:
                onClickCleanUpLogs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
