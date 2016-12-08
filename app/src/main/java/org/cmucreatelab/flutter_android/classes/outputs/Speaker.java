package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.settings.Settings;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * Speaker
 *
 * A class that represents the speaker on a flutter.
 *
 */
public class Speaker extends A_Output implements Serializable, Output {


    public static final String SPEAKER_KEY = "speaker_key";

    private static final Output.Type outputType = Type.SPEAKER;
    private static final int MINIMUM_VOLUME = 0;
    private static final int MAXIMUM_VOLUME = 100;
    private static final int MINIMUM_FREQUENCY = 262;
    private static final int MAXIMUM_FREQUENCY = 1047;

    public static final int imageId = R.mipmap.ic_launcher;

    private Settings frequencySettings;
    private Settings volumeSettings;


    public Speaker(int portNumber) {
        super(portNumber);
        frequencySettings = new Settings("f", MAXIMUM_FREQUENCY, MINIMUM_FREQUENCY);
        volumeSettings = new Settings("v", MAXIMUM_VOLUME, MINIMUM_VOLUME);
        setSettings(frequencySettings);
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


    public Settings getFrequencySettings() { return frequencySettings; }
    public Settings getVolumeSettings() { return volumeSettings; }


    public void setFrequencySettings(Settings settings) { this.frequencySettings = settings; }
    public void setVolumeSettings (Settings settings) { this.volumeSettings = settings; }

}
