package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.util.Log;

import org.cmucreatelab.android.melodysmart.DeviceHandler;
import org.cmucreatelab.android.melodysmart.MessageQueue;
import org.cmucreatelab.android.melodysmart.listeners.DataListener;
import org.cmucreatelab.android.melodysmart.listeners.DeviceListener;
import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs.UnableToConnectFlutterDialog;

/**
 * Created by mike on 2/9/17.
 *
 * FlutterDeviceHandler
 *
 * A class to manage communications with the Flutter.
 *
 */
public class FlutterDeviceHandler extends DeviceHandler<MessageQueue<MelodySmartMessage>> {

    final private GlobalHandler globalHandler;


    public FlutterDeviceHandler(GlobalHandler globalHandler) {
        super(globalHandler.appContext);
        this.globalHandler = globalHandler;
    }


    /*
     * Alright, this is a little messy, but here's what is happening:
     * When we fail to receive a response from a message sent to the device, the library makes a
     * call to DeviceHandler.disconnect, which then gracefully disconnects the app from the device.
     * However, for the user, this type of event should be treated as if a BLE Error occurred. In
     * order to bypass this, we must override the parent class's disconnect() method so that it is
     * called instead. We only want to call super.disconnect() when the user is willfully
     * disconnecting the app from the device.
     */
    @Override
    public void disconnect() {
        Log.v(Constants.LOG_TAG, "FlutterDeviceHandler.disconnect");
        UnableToConnectFlutterDialog unableToConnectFlutterDialog = UnableToConnectFlutterDialog.newInstance(UnableToConnectFlutterDialog.FlutterIssueType.TIMEOUT_DISCONNECTED);
        unableToConnectFlutterDialog.show(globalHandler.sessionHandler.getSession().getCurrentActivity().getSupportFragmentManager(), "tag");
    }


    /**
     *
     * Triggers disconnecting all devices from the running application. Use this method instead of
     * the {@link #disconnect()} method.
     *
     * @param isError determines if we are disconnecting because an error has occurred. True
     *                indicates that an error has occurred. False indicates that the disconnect was
     *                not from an error (and presumably this is what the user wanted).
     */
    public void disconnect(boolean isError) {
        if (isError) {
            disconnect();
        } else {
            super.disconnect();
        }
    }


    @Override
    public MessageQueue initializeMessageQueue() {
        return new MessageQueue(this);
    }


    @Override
    public DeviceListener initializeDeviceListener() {
        return new FlutterDeviceListener(globalHandler.sessionHandler.getSession());
    }


    @Override
    public DataListener initializeDataListener() {
        return new FlutterDataListener(globalHandler, this);
    }

}
