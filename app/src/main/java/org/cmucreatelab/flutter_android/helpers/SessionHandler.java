package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 5/31/2016.
 *
 * Session Handler
 *
 * A class that handles your session (when you select a specific device on AppLandingActivity)
 * - connect
 * - disconnect
 * - message sending
 *
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    public Session session;


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void startSession(AppLandingActivity activity, FlutterOG flutterOG) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutterOG.getDevice().getName());
        this.session = new Session(activity,flutterOG,activity,null);
        globalHandler.melodySmartDeviceHandler.connect(this.session);
    }


    // TODO @tasota delete message-sending methods (deprecated)
    public void addMessage(String msg) {
        globalHandler.melodySmartDeviceHandler.addMessage(msg);
    }


    public void addMessages(ArrayList<String> msgs) {
        globalHandler.melodySmartDeviceHandler.addMessages(msgs);
    }


    public void sendMessages() {
        globalHandler.melodySmartDeviceHandler.sendMessages();
    }


    // Getters and Setters


    // TODO @tasota delete getters/setters (deprecated)
    public FlutterOG getFlutter() { return session.flutter; }
    public BluetoothDevice getFlutterDevice() {
        return session.flutter.getDevice();
    }
    public String getFlutterName() {
        return session.flutter.getName();
    }

    public boolean isBluetoothConnected() {
        return globalHandler.melodySmartDeviceHandler.melodySmartDeviceListener.deviceConnected;
    }

    public void setFlutterConnectListener(final FlutterConnectListener flutterConnectListener) {
        this.session.flutterConnectListener = flutterConnectListener;
    }
    public void setFlutterMessageListener(final FlutterMessageListener flutterMessageListener) {
        this.session.flutterMessageListener = flutterMessageListener;
    }

}
