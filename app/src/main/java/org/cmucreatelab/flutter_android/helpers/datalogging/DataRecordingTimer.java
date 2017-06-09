package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.os.Handler;

/**
 * Created by Steve on 3/14/2017.
 *
 * A timer for checking if the data logging recording has finished
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
    private int initial;
    private TimeExpireListener timeExpireListener;


    public DataRecordingTimer(int intervalInMs) {
        this.milliseconds = intervalInMs;
        initial = 0;
    }


    public void startTimer(TimeExpireListener timeExpireListener) {
        this.timeExpireListener = timeExpireListener;
        stopTimer();
        if (initial == 0) {
            handler.post(runnable);
            initial--;
        }
        else
            handler.postDelayed(runnable, milliseconds);
    }


    public void stopTimer() {
        if (timeExpireListener != null) {
            handler.removeCallbacks(runnable);
        }
    }


    public interface TimeExpireListener {
        public void timerExpired();
    }

}
