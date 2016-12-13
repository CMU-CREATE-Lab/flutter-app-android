package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 12/13/2016.
 */
public class Volume extends A_Output {

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;
    private static final String TYPE = "v";
    private static final Output.Type outputType = Type.VOLUME;

    public static final String LED_KEY = "volume_speaker_key";
    public static final int imageId = R.mipmap.ic_launcher;


    public Volume(int portNumber) {
        super(TYPE, MAXIMUM, MINIMUM, portNumber);
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
