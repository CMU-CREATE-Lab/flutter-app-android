package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.util.Log;

import org.cmucreatelab.android.melodysmart.DeviceHandler;
import org.cmucreatelab.android.melodysmart.MessageQueue;
import org.cmucreatelab.android.melodysmart.listeners.DataListener;
import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.ArrayList;

/**
 * Created by mike on 2/9/17.
 */
public class FlutterDataListener extends DataListener<MessageQueue> {

    final private Session session;
    final private DeviceHandler parent;

    
    public FlutterDataListener(Session session, DeviceHandler parent) {
        this.session = session;
        this.parent = parent;
    }
    
    
    @Override
    public void onConnected() {
        Log.v(Constants.LOG_TAG, "FlutterDataListener.onConnected");

        parent.getDataService().enableNotifications(true);

        // TODO @tasota this clears the Flutter simulation state when you connect, but really should just read in the state
        parent.addMessage(MessageConstructor.constructStopSimulateData());
        // read sensor types
        Sensor[] sensors = session.getFlutter().getSensors();
        for (Sensor sensor : sensors) {
            parent.addMessage(MessageConstructor.constructReadInputType(sensor));
        }
        // read outputs
        ArrayList<Output> outputs = session.getFlutter().getOutputs();
        for (Output output : outputs) {
            parent.addMessage(MessageConstructor.constructReadOutputState(output));
        }

        session.getFlutterConnectListener().onFlutterConnected();
    }


    @Override
    public void onMessageReceived(MelodySmartMessage request, String response) {
        if (Constants.IGNORE_READ_SENSORS && request.getRequest().charAt(0) == FlutterProtocol.Commands.READ_SENSOR_VALUES) {
            return;
        }
        Log.v(Constants.LOG_TAG, "FlutterDataListener.onMessageReceived");
        // handle parse
        session.onFlutterMessageReceived(request.getRequest(), response);
        // update views
        if (session.getFlutterMessageListener() != null) {
            session.getFlutterMessageListener().onFlutterMessageReceived(request.getRequest(), response);
        } else {
            Log.w(Constants.LOG_TAG,"Tried to call callback onFlutterMessageReceived but session FlutterMessageListener is null.");
        }
    }

}
