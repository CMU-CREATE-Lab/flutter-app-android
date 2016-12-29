package org.cmucreatelab.flutter_android.helpers.melodysmart.listeners;

import android.util.Log;

import com.bluecreation.melodysmart.DataService;

import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.melodysmart.DeviceHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 12/28/16.
 *
 * Creates a DataService.Listener instance using information from a Session.
 *
 */
public class DataListener implements DataService.Listener {

    private Session mSession;
    private DeviceHandler parent;
    private boolean serviceConnected;


    public boolean isServiceConnected() {
        return serviceConnected;
    }


    public DataListener(Session session, DeviceHandler parent) {
        this.mSession = session;
        this.parent = parent;
        serviceConnected = false;
    }


    @Override
    public void onConnected(final boolean isFound) {
        Log.v(Constants.LOG_TAG,"DataListener.onConnected isFound="+isFound);
        serviceConnected = isFound;

        if (isFound) {
            parent.getDataService().enableNotifications(true);
        }
        mSession.getFlutterConnectListener().onFlutterConnected();
    }


    @Override
    public void onReceived(final byte[] bytes) {
        String response = new String(bytes);
        Log.v(Constants.LOG_TAG,"DataListener.onReceived="+response);
        mSession.getFlutterMessageListener().onFlutterMessageReceived(response);
    }

}
