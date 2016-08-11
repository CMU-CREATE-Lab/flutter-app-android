package org.cmucreatelab.flutter_android.classes;

/**
 * Created by Steve on 6/20/2016.
 *
 * Speaker
 *
 * A class that represents the speaker on a flutter.
 *
 */
public class Speaker {


    private static final int MINIMUM_VOLUME = 0;
    private static final int MAXIMUM_VOLUME = 100;
    private static final int MINIMUM_FREQUENCY = 0;
    private static final int MAXIMUM_FREQUENCY = 20000;

    private int volume;
    private int frequency;


    public Speaker(int volume, int frequency) {
        this.setVolume(volume);
        this.setFrequency(frequency);
    }


    public void setVolume(int volume) {
        if (volume >= MINIMUM_VOLUME && volume <= MAXIMUM_VOLUME) {
            this.volume = volume;
        }
    }
    public int getVolume() {
        return volume;
    }
    public void setFrequency(int frequency) {
        if (frequency >= MINIMUM_FREQUENCY && frequency <= MAXIMUM_FREQUENCY) {
            this.frequency = frequency;
        }
    }
    public int getFrequency() {
        return this.frequency;
    }
}
