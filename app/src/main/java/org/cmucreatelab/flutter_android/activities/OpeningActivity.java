package org.cmucreatelab.flutter_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on dunno...it wasn't automatically generated. (I wonder what I did differently.)
 *
 * OpeningActivity
 *
 * An activity which handles the opening screen.
 *
 */
public class OpeningActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.image_connect_flutter)
    public void onClickConnectFlutter() {
        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.image_view_data_logs)
    public void onClickViewDataLogs() {
        Intent intent = new Intent(getApplicationContext(), DataLogsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
