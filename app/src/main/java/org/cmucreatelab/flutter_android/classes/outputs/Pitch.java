package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 12/13/2016.
 */
public class Pitch extends A_Output {

    public static final int MINIMUM = 262;
    public static final int MAXIMUM = 1047;
    private static final String TYPE = "f";
    private static final Output.Type outputType = Type.PITCH;

    public static final String LED_KEY = "pitch_speaker_key";
    public static final int imageId = R.mipmap.ic_launcher;


    public Pitch(int portNumber) {
        super(TYPE, MAXIMUM, MINIMUM, portNumber);
    }


    @Override
    public String getProtocolString() {
        return TYPE;
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
        return MAXIMUM;
    }


    @Override
    public int getMin() {
        return MINIMUM;
    }

}
