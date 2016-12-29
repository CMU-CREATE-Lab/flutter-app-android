package org.cmucreatelab.flutter_android.activities.abstract_activities;

import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Steve on 11/23/2016.
 */
public abstract class BaseSensorReadingActivity extends BaseNavigationActivity implements FlutterMessageListener, Serializable {


    private Timer timer;


    protected void startSensorReading() {
        if (timer != null) {
            timer.cancel();
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
                globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.READ_SENSOR);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }


    protected void stopSensorReading() {
        timer.cancel();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (GlobalHandler.getInstance(getApplicationContext()).melodySmartDeviceHandler.isConnected())
            stopSensorReading();
    }

}
