package org.cmucreatelab.flutter_android.classes;

import android.app.Activity;

import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;

/**
 * Created by mike on 12/28/16.
 *
 * Provides access to the current session's flutter and activities/listeners.
 *
 */
public class Session {

    private Activity currentActivity;
    private FlutterOG flutter;
    private FlutterConnectListener flutterConnectListener;
    private FlutterMessageListener flutterMessageListener;
    // getters/setters
    public Activity getCurrentActivity() { return currentActivity; }
    public FlutterOG getFlutter() { return flutter; }
    public FlutterConnectListener getFlutterConnectListener() { return flutterConnectListener; }
    public FlutterMessageListener getFlutterMessageListener() { return flutterMessageListener; }
    public void setCurrentActivity(Activity currentActivity) { this.currentActivity = currentActivity; }
    public void setFlutter(FlutterOG flutter) { this.flutter = flutter; }
    public void setFlutterMessageListener(FlutterMessageListener flutterMessageListener) { this.flutterMessageListener = flutterMessageListener; }
    public void setFlutterConnectListener(FlutterConnectListener flutterConnectListener) { this.flutterConnectListener = flutterConnectListener; }


    public Session(Activity currentActivity, FlutterOG flutter, FlutterConnectListener flutterConnectListener, FlutterMessageListener flutterMessageListener) {
        this.currentActivity = currentActivity;
        this.flutter = flutter;
        this.flutterConnectListener = flutterConnectListener;
        this.flutterMessageListener = flutterMessageListener;
    }

}
