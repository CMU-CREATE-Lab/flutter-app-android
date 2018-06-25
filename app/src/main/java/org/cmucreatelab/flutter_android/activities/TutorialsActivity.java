package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.record_data_wizard.FlutterSampleDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.record_data_wizard.ReviewRecordingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.BlueSensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.DataSnapshotDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.RecordingWarningSensorDialog;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv.
 *
 * TutorialsActivity
 *
 */
public class TutorialsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);

        ButterKnife.bind(this);

        // construct toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        // this is checking for if the layout being used is layout-large. if the view is null, we must be using non-large layout
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g));
        setSupportActionBar(toolbar);

        WebView web = (WebView) findViewById(R.id.web_view_tutorials);
        web.loadUrl("file:///android_asset/tutorials.html");
    }

}
