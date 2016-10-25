package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 6/20/2016.
 *
 * Speaker
 *
 * A class that represents the speaker on a flutter.
 *
 */
public class Speaker extends A_Output implements Output {


    private static final Output.Type outputType = Type.SPEAKER;
    private static final int MINIMUM_VOLUME = 0;
    private static final int MAXIMUM_VOLUME = 100;
    private static final int MINIMUM_FREQUENCY = 0;
    private static final int MAXIMUM_FREQUENCY = 20000;

    public static final int imageId = R.mipmap.ic_launcher;

    private int volume;
    private int frequency;

    public Speaker() {
        this.volume = MAXIMUM_VOLUME / 2;
        this.frequency = MAXIMUM_FREQUENCY / 2;
    }


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


    @Override
    public Type getOutputType() {
        return outputType;
    }


    @Override
    public int getOutputImageId() {
        return imageId;
    }


    @Override
    public int getMax() {
        return MAXIMUM_VOLUME;
    }


    @Override
    public int getMin() {
        return MINIMUM_VOLUME;
    }

}
