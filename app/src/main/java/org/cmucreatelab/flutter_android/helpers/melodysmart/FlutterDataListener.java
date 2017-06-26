package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.util.Log;

import org.cmucreatelab.android.melodysmart.DeviceHandler;
import org.cmucreatelab.android.melodysmart.MessageQueue;
import org.cmucreatelab.android.melodysmart.listeners.DataListener;
import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 2/9/17.
 */
public class FlutterDataListener extends DataListener<MelodySmartMessage, MessageQueue<MelodySmartMessage>> {

    private final GlobalHandler globalHandler;
    private final DeviceHandler parent;
    /**
     * Keeps track of how we are handling received messages. Before connection finishes, we need
     * to handle the response as well as keep track of which messages we are still waiting to
     * get a response from.
     */
    private MessageReceiver currentMessageReceiver;
    private final MessageReceiver messageReceiverBeforeConnected;
    private final MessageReceiver messageReceiverAfterConnected;


    private void handleMessageResponse(MelodySmartMessage message) {
        String request,response;
        final Session session = globalHandler.sessionHandler.getSession();

        if (message.getRequests().size() == 0 || message.getResponses().size() == 0) {
            Log.e(Constants.LOG_TAG,"onMessageReceived is request or response size of zero.");
            return;
        }
        request = message.getRequests().get(0);
        response = message.getResponses().get(0);

        if (Constants.IGNORE_READ_SENSORS && request.charAt(0) == FlutterProtocol.Commands.READ_SENSOR_VALUES) {
            return;
        }

        // handle parse and react appropriately
        session.onFlutterMessageReceived(request, response);
    }

    
    public FlutterDataListener(final GlobalHandler globalHandler, final DeviceHandler parent) {
        super(parent);
        this.globalHandler = globalHandler;
        this.parent = parent;
        this.messageReceiverBeforeConnected = new MessageReceiver() {

            private final ArrayList<MelodySmartMessage> messages = new ArrayList<>();
            private final ArrayList<MelodySmartMessage> messagesReceived = new ArrayList<>();


            @Override
            public void addMessages(List<MelodySmartMessage> messages) {
                this.messages.addAll(messages);
            }


            // ASSERT: Session is not null
            @Override
            public void onMessageReceived(MelodySmartMessage message) {
                // perform actions based on response
                handleMessageResponse(message);

                // updatedPoints received messages progress
                if (message.getRequests().size() > 0) {
                    String request = message.getRequests().get(0);
                    char protocolCommand = request.charAt(0);
                    switch (protocolCommand) {
                        case 'M':
                        case 'Y':
                            globalHandler.sessionHandler.updateProgressDialogMessage(globalHandler.sessionHandler.getSession().getCurrentActivity(), "Loading Sensor Types");
                            break;
                        case 'O':
                            globalHandler.sessionHandler.updateProgressDialogMessage(globalHandler.sessionHandler.getSession().getCurrentActivity(), "Loading Outputs");
                            break;
                        default:
                            Log.e(Constants.LOG_TAG, "received response from an unexpected protocol command");
                            break;
                    }
                }

                // keep track of what is finished
                messagesReceived.add(message);
                if (messagesReceived.containsAll(messages)) {
                    // if we received all of the desired messages, switch listeners.
                    Log.v(Constants.LOG_TAG,"all messages received; now moving to messageReceiverAfterConnected");
                    FlutterDataListener.this.currentMessageReceiver = messageReceiverAfterConnected;
                    globalHandler.sessionHandler.getSession().getFlutterConnectListener().onFlutterConnected();
                }
            }

        };
        this.messageReceiverAfterConnected = new MessageReceiver() {


            @Override
            public void addMessages(List<MelodySmartMessage> messages) {
                Log.e(Constants.LOG_TAG,"addMessages called when it shouldn't have been.");
            }


            @Override
            public void onMessageReceived(MelodySmartMessage message) {
                // after connected, we only handle/parse the message response
                handleMessageResponse(message);
            }

        };
        this.currentMessageReceiver = messageReceiverBeforeConnected;
    }
    
    
    @Override
    public void onConnected() {
        Log.v(Constants.LOG_TAG, "FlutterDataListener.onConnected");
        final Session session = globalHandler.sessionHandler.getSession();
        parent.getDataService().enableNotifications(true);

        // create the list of messages that we want to handle upon connection
        ArrayList<MelodySmartMessage> messages = new ArrayList<>();
        // TODO @tasota this clears the Flutter simulation state when you connect, but really should just read in the state
        messages.add(MessageConstructor.constructStopSimulateData());
        // read sensor types
        Sensor[] sensors = session.getFlutter().getSensors();
        for (Sensor sensor : sensors) {
            messages.add(MessageConstructor.constructReadInputType(sensor));
        }
        // read outputs
        ArrayList<Output> outputs = session.getFlutter().getOutputs();
        for (Output output : outputs) {
            messages.add(MessageConstructor.constructReadOutputState(output));
        }

        // add messages to MessageReceiver and then add to MessageQueue
        messageReceiverBeforeConnected.addMessages(messages);
        for (MelodySmartMessage m : messages) {
            parent.addMessage(m);
        }
    }


    @Override
    public void onMessageReceived(MelodySmartMessage message) {
        this.currentMessageReceiver.onMessageReceived(message);
    }


    /**
     * Class listener for handling DataListener.onMessageReceived
     */
    private abstract class MessageReceiver {

        /**
         * Keep track of the messages that you are waiting to receive. Only implemented before the
         * connection process (and initial setup) has finished.
         * @param messages The messages you are waiting to receive.
         */
        public abstract void addMessages(List<MelodySmartMessage> messages);

        public abstract void onMessageReceived(MelodySmartMessage message);

    }

}
