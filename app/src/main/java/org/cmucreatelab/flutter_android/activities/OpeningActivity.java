package org.cmucreatelab.flutter_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpeningActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.image_connectFlutter)
    public void onClickConnectFlutter() {
        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
