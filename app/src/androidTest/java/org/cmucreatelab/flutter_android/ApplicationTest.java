package org.cmucreatelab.flutter_android;

import android.app.Activity;
import android.app.Application;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.test.ApplicationTestCase;
import android.view.View;

import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    @Rule
    public ActivityTestRule<AppLandingActivity> appLandingRule = new ActivityTestRule<>(AppLandingActivity.class);

    @Rule
    public ActivityTestRule<SensorsActivity> sensorsRule = new ActivityTestRule<>(SensorsActivity.class);

    @Rule
    public ActivityTestRule<RobotActivity> robotRule = new ActivityTestRule<>(RobotActivity.class);

    @Rule
    public ActivityTestRule<DataLogsActivity> dataLogsRule = new ActivityTestRule<>(DataLogsActivity.class);


    public ApplicationTest() {
        super(Application.class);
    }


    @Test
    public void ensureNavigationDrawerIsPresentAppLandingActivity() throws Exception {
        ensureNavigationDrawerIsPresent(appLandingRule);
    }


    @Test
    public void ensureNavigationDrawerIsPresentSensorsActiviy() throws Exception {
        ensureNavigationDrawerIsPresent(sensorsRule);
    }


    @Test
    public void ensureNavigationDrawerIsPresentRobotActivity() throws Exception {
        ensureNavigationDrawerIsPresent(robotRule);
    }


    @Test
    public void ensureNavigationDrawerIsPresentDataLogsActivity() throws Exception {
        ensureNavigationDrawerIsPresent(dataLogsRule);
    }


    private void ensureNavigationDrawerIsPresent(ActivityTestRule rule) throws Exception {
        Activity activity = rule.getActivity();
        View drawerLayout = activity.findViewById(R.id.drawer_layout);
        assertThat(drawerLayout, IsNull.notNullValue());
        assertThat(drawerLayout, instanceOf(DrawerLayout.class));
    }
}