package org.cmucreatelab.flutter_android.activities.abstract_activities;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 8/31/2016.
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

}
