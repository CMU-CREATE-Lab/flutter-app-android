package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 5/31/2016.
 *
 * Session Handler
 *
 * A class that handles your session after selecting a device on AppLandingActivity
 *
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    private Session session;


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void startSession(AppLandingActivity activity, FlutterOG flutterOG) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutterOG.getDevice().getName());
        this.session = new Session(activity,flutterOG,activity,null);
        globalHandler.melodySmartDeviceHandler.connect(this.getSession());
    }


    public void setCurrentActivity(BaseNavigationActivity activity) {
        if (session != null) {
            session.setCurrentActivity(activity);
        }
    }


    public Session getSession() {
        return session;
    }

}
