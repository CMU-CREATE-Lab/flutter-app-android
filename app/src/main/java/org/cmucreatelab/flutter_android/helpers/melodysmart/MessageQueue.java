package org.cmucreatelab.flutter_android.helpers.melodysmart;

import android.util.Log;

import com.bluecreation.melodysmart.DataService;

import org.cmucreatelab.flutter_android.classes.FlutterMessage;
import org.cmucreatelab.flutter_android.classes.Timer;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mike on 12/28/16.
 *
 * Uses MelodySmart's DataService to send messages. Messages are handled FIFO with a slight delay between sending.
 */
public class MessageQueue {

    private ConcurrentLinkedQueue<FlutterMessage> messages;
    private FlutterMessage currentMessage = null; // the current message that is presumably being processed by DataService
    private boolean isWaitingForResponse = false;
    private Timer messageSendingTimer;
    private Timer messageTimeout;

    private static final int MESSAGE_SENDING_TIMER_DELAY_IN_MILLISECONDS = 100;
    // if we do not receive a response within this time, recover
    private static final int MESSAGE_TIMEOUT_WAIT_IN_MILLISECONDS = 3000;


    MessageQueue(final DataService dataService) {
        messages = new ConcurrentLinkedQueue<>();
        messageTimeout = new Timer(MESSAGE_TIMEOUT_WAIT_IN_MILLISECONDS) {
            @Override
            public void timerExpires() {
                if (currentMessage != null) {
                    Log.e(Constants.LOG_TAG,"messageTimeout timerExpires; will not process request="+currentMessage.getRequest());
                    // TODO @tasota this should likely trigger disconnecting from the Flutter
                }
                notifyMessageReceived();
            }
        };
        messageSendingTimer = new Timer(MESSAGE_SENDING_TIMER_DELAY_IN_MILLISECONDS) {
            @Override
            public void timerExpires() {
                if (!messages.isEmpty()) {
                    isWaitingForResponse = true;
                    messageTimeout.startTimer();
                    currentMessage = messages.poll();
                    Log.v(Constants.LOG_TAG,"messageSendingTimer timerExpires: SEND: '"+currentMessage.getRequest()+"'");
                    dataService.send(currentMessage.getRequest().getBytes());
                }
            }
        };
    }


    /**
     * Call to notify the MessageQueue that the message response was received.
     *
     * @return the requested FlutterMessage associated with message response.
     */
    public synchronized FlutterMessage notifyMessageReceived() {
        isWaitingForResponse = false;
        messageTimeout.stopTimer();
        FlutterMessage result = this.currentMessage;
        this.currentMessage = null;
        sendNextMessage();
        return result;
    }


    // message-sending


    void addMessage(FlutterMessage message) {
        if (Constants.IGNORE_READ_SENSORS && message.getRequest().equals("r")) {
            return;
        }
        messages.add(message);
        sendNextMessage();
    }


    private void sendNextMessage() {
        if (isWaitingForResponse) {
            Log.w(Constants.LOG_TAG,"refusing to sendNextMessage since messageQueue.isWaitingForResponse == true");
            return;
        }
        messageSendingTimer.startTimer();
    }

}
