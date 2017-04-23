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


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void createProgressDialog(BaseNavigationActivity activity) {
        if (progressDialog != null) {
            dismissProgressDialog();
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


    public void startSession(AppLandingActivity activity, Flutter flutter) {
        Log.d(Constants.LOG_TAG, "Starting session with " + flutter.getBluetoothDevice().getName());
        this.session = new Session(activity, flutter,activity,null);
        createProgressDialog(activity);
        globalHandler.melodySmartDeviceHandler.connect(this.getSession().getFlutter().getBluetoothDevice());
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