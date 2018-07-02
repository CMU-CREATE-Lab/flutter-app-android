package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;

import butterknife.ButterKnife;

/**
 * Created by Parv.
 *
 * GlossaryActivity
 *
 * An activity to display the glossary for terms related to Flutter.
 *
 */
public class GlossaryActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        ButterKnife.bind(this);

        // construct toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        // this is checking for if the layout being used is layout-large. if the view is null, we must be using non-large layout
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g));
        setSupportActionBar(toolbar);

        WebView web = (WebView) findViewById(R.id.web_view_glossary);
        web.loadUrl("file:///android_asset/glossary.html");
    }

}
