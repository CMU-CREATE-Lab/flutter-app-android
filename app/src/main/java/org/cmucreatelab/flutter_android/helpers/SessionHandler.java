package org.cmucreatelab.flutter_android.helpers;

import android.app.ProgressDialog;
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
// TODO - Too much is happening in here I think, I'd like to refactor it eventually -  split some things up
// TODO - I would like this class to eventually be a container that holds the flutter instance, data logs, and any other object instances we need throughout the session of the app.
// TODO - Refactor message sending and actually make use of the Message class
// TODO - Separate class for handling connecting and disconnecting (the more we split this up the better)
// I would like to have a message handler class that handles all of the messages
// Maybe a MessageSender class that does the actual sending of messages and the Message Handler
// will just handle which messages are being sent to the sender.
// This would clean up the DataLoggingHandler and anywhere else we send messages.
// Wish I thought of that before I wrote all of this.. :/
public class SessionHandler {

    private GlobalHandler globalHandler;
    private Session session;
    private ProgressDialog progressDialog;


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void createProgressDialog(BaseNavigationActivity activity) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        this.progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Loading");
        updateProgressDialogMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public void updateProgressDialogMessage(String message) {
        if (progressDialog == null) {
            Log.e(Constants.LOG_TAG,"called updateProgressDialogMessage but progressDialog is null.");
            return;
        }
        progressDialog.setMessage(message);
    }


    public void dismissProgressDialog() {
        if (progressDialog == null) {
            Log.e(Constants.LOG_TAG,"called dismissProgressDialog but progressDialog is null.");
            return;
        }
        progressDialog.dismiss();
        progressDialog = null;
    }


    public void startSession(AppLandingActivity activity, FlutterOG flutterOG) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutterOG.getDevice().getName());
        this.session = new Session(activity,flutterOG,activity,null);
        createProgressDialog(activity);
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