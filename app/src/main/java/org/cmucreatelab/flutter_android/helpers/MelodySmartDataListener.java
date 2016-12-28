package org.cmucreatelab.flutter_android.helpers;

import android.util.Log;

import com.bluecreation.melodysmart.DataService;

import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 12/28/16.
 */

public class MelodySmartDataListener implements DataService.Listener {

    private Session mSession;
    private MelodySmartDeviceHandler parent;
    public boolean serviceConnected;


    public MelodySmartDataListener(Session session, MelodySmartDeviceHandler parent) {
        this.mSession = session;
        this.parent = parent;
        serviceConnected = false;
    }


    @Override
    public void onConnected(final boolean isFound) {
        Log.v(Constants.LOG_TAG,"MelodySmartDataListener.onConnected isFound="+isFound);
        serviceConnected = isFound;

        if (isFound) {
            parent.getDataService().enableNotifications(true);
        }
        mSession.flutterConnectListener.onFlutterConnected();
    }


    @Override
    public void onReceived(final byte[] bytes) {
        mSession.flutterMessageListener.onFlutterMessageReceived(new String(bytes));
        if (!parent.messages.isEmpty()) {
            String msg = parent.messages.poll();
            // So even though there is a callback so I do not send messages on top of each other,
            // the flutter still seems to need some time in order to send all of the messages successfully.
            // For example, making an led relationship we need to send three separate messages for each color (rgb)
            // This is why I put a simple sleep to give the flutter some time.
            // Without this, only the last color would be set, blue.
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(Constants.LOG_TAG, msg);
            parent.getDataService().send(msg.getBytes());
        }
    }

}
