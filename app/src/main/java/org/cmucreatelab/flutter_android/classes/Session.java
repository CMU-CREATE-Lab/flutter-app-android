package org.cmucreatelab.flutter_android.classes;

import android.app.Activity;

import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;

/**
 * Created by mike on 12/28/16.
 */

public class Session {

    public Activity currentActivity;
    public FlutterOG flutter;
    public FlutterConnectListener flutterConnectListener;
    public FlutterMessageListener flutterMessageListener;


    public Session(Activity currentActivity, FlutterOG flutter, FlutterConnectListener flutterConnectListener, FlutterMessageListener flutterMessageListener) {
        this.currentActivity = currentActivity;
        this.flutter = flutter;
        this.flutterConnectListener = flutterConnectListener;
        this.flutterMessageListener = flutterMessageListener;
    }

}
