package org.cmucreatelab.flutter_android.helpers;

import android.util.Log;

import com.bluecreation.melodysmart.DataService;

import org.cmucreatelab.flutter_android.classes.Timer;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mike on 12/28/16.
 */

public class MelodySmartMessageQueue {

    protected ConcurrentLinkedQueue<String> messages;
    private DataService dataService;
    private Timer messageSendingTimer;

    // (from steve)
    // So even though there is a callback so I do not send messages on top of each other,
    // the flutter still seems to need some time in order to send all of the messages successfully.
    // For example, making an led relationship we need to send three separate messages for each color (rgb)
    // This is why I put a simple sleep to give the flutter some time.
    // Without this, only the last color would be set, blue.
    private static final int TIMER_LENGTH_IN_MILLISECONDS = 300;


    public MelodySmartMessageQueue(final DataService dataService) {
        this.dataService = dataService;
        messages = new ConcurrentLinkedQueue<>();
        messageSendingTimer = new Timer(TIMER_LENGTH_IN_MILLISECONDS) {
            @Override
            public void timerExpires() {
                if (!messages.isEmpty()) {
                    String message = messages.poll();
                    Log.v(Constants.LOG_TAG,"MelodySmartMessageQueue timerExpires: SEND: '"+message+"'");
                    dataService.send(message.getBytes());
                    startTimer();
                }
            }
        };
    }


    // message-sending


    public void addMessage(String msg) {
        messages.add(msg);
        messageSendingTimer.startTimer();
    }


    public void addMessages(ArrayList<String> msgs) {
        messages.addAll(msgs);
        messageSendingTimer.startTimer();
    }
}
