package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;

import butterknife.ButterKnife;

public class SpeakerActivity extends BaseNavigationActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        ButterKnife.bind(this);
    }

}
