package org.cmucreatelab.flutter_android.activities.abstract_activities;

/**
 * Created by Steve on 8/23/2016.
 *
 * BaseFlutterActivity
 *
 * An abstract activity used to appropriately disconnect from a flutter.
 *
 */
public abstract class BaseFlutterActivity extends BaseNavigationActivity {


    @Override
    public void onBackPressed() {
        globalHandler.sessionHandler.release();
        super.onBackPressed();
        finish();
    }

}
