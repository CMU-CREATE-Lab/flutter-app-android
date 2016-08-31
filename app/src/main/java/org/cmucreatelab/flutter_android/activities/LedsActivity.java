package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;

// TODO - may make another abstract activity for this toolbar menu clicking
public class LedsActivity extends BaseNavigationActivity {


    private static final String HELP = "help";
    private static final String ADVANCED = "advanced";


    private Toolbar.OnMenuItemClickListener toolbarClick = new Toolbar.OnMenuItemClickListener() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leds);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // internal toolbars
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar1.setTitle(R.string.led_1);
        toolbar1.inflateMenu(R.menu.menu_servo_led);
        toolbar1.setOnMenuItemClickListener(toolbarClick);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitle(R.string.led_2);
        toolbar2.inflateMenu(R.menu.menu_servo_led);
        toolbar2.setOnMenuItemClickListener(toolbarClick);

        Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar3);
        toolbar3.setTitle(R.string.led_3);
        toolbar3.inflateMenu(R.menu.menu_servo_led);
        toolbar3.setOnMenuItemClickListener(toolbarClick);
    }
}
