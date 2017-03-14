package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.os.Handler;

/**
 * Created by Steve on 3/14/2017.
 */

public class DataRecordingTimer {

    final private Handler handler = new Handler();
    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeExpireListener.timerExpired();
        }
    };

    private int milliseconds;
    private TimeExpireListener timeExpireListener;


    public DataRecordingTimer(int intervalInMs, TimeExpireListener timeExpireListener) {
        this.milliseconds = intervalInMs;
        this.timeExpireListener = timeExpireListener;
    }


    public void startTimer() {
        stopTimer();
        handler.postDelayed(runnable, milliseconds);
    }


    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }


    public interface TimeExpireListener {
        public void timerExpired();
    }

}
