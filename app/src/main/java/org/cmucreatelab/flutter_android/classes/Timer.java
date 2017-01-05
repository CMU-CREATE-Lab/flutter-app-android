package org.cmucreatelab.flutter_android.classes;

import android.os.Handler;

/**
 * Created by mike on 10/27/15.
 */
public abstract class Timer {

    // class attributes
    final private Handler handler = new Handler();
    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timerExpires();
        }
    };
    private int timerInterval; // length of time for the timer (in milliseconds)


    // class constructor
    public Timer(int timerInterval) {
        this.timerInterval = timerInterval;
    }


    public void startTimer() {
        stopTimer();
        handler.postDelayed(runnable, timerInterval);
    }


    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }


    // actions to perform when timer expires
    public abstract void timerExpires();

}
