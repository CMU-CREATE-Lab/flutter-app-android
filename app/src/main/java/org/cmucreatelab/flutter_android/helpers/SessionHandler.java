package org.cmucreatelab.flutter_android.helpers;

import android.app.ProgressDialog;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
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
    private ProgressDialog progressDialog;
    private boolean sessionStarted;


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sessionStarted = false;
    }


    // TODO @tasota deprecated; Activity not needed as parameter to control ProgressDialog
    public void createProgressDialog(BaseNavigationActivity activity) {
        createProgressDialog();
    }


    public void createProgressDialog() {
        if (progressDialog != null) {
            dismissProgressDialog();
        }
        if (!sessionStarted) {
            Log.e(Constants.LOG_TAG,"createProgressDialog with sessionStarted=false");
            return;
        }
        session.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(session.getCurrentActivity());
                progressDialog.setTitle("Loading");
                updateProgressDialogMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }


    public void updateProgressDialogMessage(final String message) {
        if (progressDialog == null) {
            Log.e(Constants.LOG_TAG,"called updateProgressDialogMessage but progressDialog is null.");
            return;
        }
        if (!sessionStarted) {
            Log.e(Constants.LOG_TAG,"updateProgressDialogMessage with sessionStarted=false");
            return;
        }
        session.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
            }
        });
    }


    public void dismissProgressDialog() {
        if (progressDialog == null) {
            Log.e(Constants.LOG_TAG,"called dismissProgressDialog but progressDialog is null.");
            return;
        }
        progressDialog.dismiss();
    }


    public void startSession(AppLandingActivity activity, Flutter flutter) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutter.getBluetoothDevice().getName());
        this.session = new Session(activity, flutter,activity,null);
        this.sessionStarted = true;
        createProgressDialog(activity);
        globalHandler.melodySmartDeviceHandler.connect(this.getSession().getFlutter().getBluetoothDevice());
    }


    public void stopSession() {
        this.sessionStarted = false;
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