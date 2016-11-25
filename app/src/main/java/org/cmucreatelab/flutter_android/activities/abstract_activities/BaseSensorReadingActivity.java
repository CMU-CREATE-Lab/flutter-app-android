package org.cmucreatelab.flutter_android.activities.abstract_activities;

import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Steve on 11/23/2016.
 */
public abstract class BaseSensorReadingActivity extends BaseNavigationActivity {


    private Timer timer;


    protected void startSensorReading() {
        if (timer != null) {
            timer.cancel();
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                globalHandler.sessionHandler.addMessage(MessageConstructor.READ_SENSOR);
                globalHandler.sessionHandler.sendMessages();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 750);
    }


    protected void stopSensorReading() {
        timer.cancel();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (globalHandler.sessionHandler.isBluetoothConnected)
            stopSensorReading();
    }

}
