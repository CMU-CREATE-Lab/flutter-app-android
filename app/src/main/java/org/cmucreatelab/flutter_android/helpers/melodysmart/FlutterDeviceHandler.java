package org.cmucreatelab.flutter_android.helpers.melodysmart;

import org.cmucreatelab.android.melodysmart.DeviceHandler;
import org.cmucreatelab.android.melodysmart.MessageQueue;
import org.cmucreatelab.android.melodysmart.listeners.DataListener;
import org.cmucreatelab.android.melodysmart.listeners.DeviceListener;
import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

/**
 * Created by mike on 2/9/17.
 */
public class FlutterDeviceHandler extends DeviceHandler<MessageQueue<MelodySmartMessage>> {

    final private GlobalHandler globalHandler;


    public FlutterDeviceHandler(GlobalHandler globalHandler) {
        super(globalHandler.appContext);
        this.globalHandler = globalHandler;
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
        return new FlutterDataListener(globalHandler.sessionHandler.getSession(), this);
    }

}
