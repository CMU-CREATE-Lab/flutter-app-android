package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO - make an adapter for loading the data logs saved on kindle
// TODO - We need to make a database to hold the data logs
public class DataLogsActivity extends BaseNavigationActivity {

    private GlobalHandler globalHandler;

    private DataSet dataSetOnFlutter;
    private FlutterOG flutter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logs);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this);

        flutter = globalHandler.sessionHandler.getFlutter();
        dataSetOnFlutter = flutter.getDataSet();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_data));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        TextView logTitle = (TextView) findViewById(R.id.text_current_device_title);
        TextView textLogName = (TextView) findViewById(R.id.text_current_log_name);
        TextView textLogPoints = (TextView) findViewById(R.id.text_num_points);

        logTitle.setText(getString(R.string.on) + " " + flutter.getName() + " " + getString(R.string.flutter));
        textLogName.setText(dataSetOnFlutter.getDataName());
        textLogPoints.setText(String.valueOf(dataSetOnFlutter.getData().size()));
    }


    @OnClick(R.id.text_open_log)
    public void onClickTextOpenLog() {
        Log.d(Constants.LOG_TAG, "onClickTextOpenLog");
    }


    @OnClick(R.id.text_send_log)
    public void onClickTextSendLog() {
        Log.d(Constants.LOG_TAG, "onClickTextSendLog");
    }


    @OnClick(R.id.text_clean_up)
    public void onClickTextCleanUp() {
        Log.d(Constants.LOG_TAG, "onClickTextCleanUp");
    }


    @OnClick(R.id.text_record_data)
    public void onClickTextRecordData() {
        Log.d(Constants.LOG_TAG, "onClickTextRecordData");
    }

}
