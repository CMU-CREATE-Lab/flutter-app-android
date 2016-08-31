package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 8/31/2016.
 *
 * BaseServoLedActivity
 *
 * An abstract activity used for the extremely similar operations of the Servo and Led activities.
 *
 */
public abstract class BaseServoLedActivity extends BaseNavigationActivity {


    private static final String HELP = "help";
    private static final String ADVANCED = "advanced";


    protected Toolbar.OnMenuItemClickListener toolbarClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(Constants.LOG_TAG, "onClick " + item.toString());
            switch (item.toString()) {
                case HELP:
                    return false;
                case ADVANCED:
                    return false;
                default:
                    return false;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // listeners


    public void onClickSelectSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectSensor");
        Log.d(Constants.LOG_TAG, view.toString());
    }


    public void onClickSelectRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectRelationship");
        Log.d(Constants.LOG_TAG, view.toString());
    }


    public void onClickSelectHighValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectHighValue");
        Log.d(Constants.LOG_TAG, view.toString());
    }


    public void onClickSelectLowValue(View view) {
        Log.d(Constants.LOG_TAG, "onClickSelectLowValue");
        Log.d(Constants.LOG_TAG, view.toString());
    }

}
