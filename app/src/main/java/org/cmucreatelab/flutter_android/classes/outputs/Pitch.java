package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 12/13/2016.
 */
public class Pitch extends Output implements Serializable {

    private static final int MINIMUM = 262;
    private static final int MAXIMUM = 1047;
    private static final String TYPE = "f";
    private static final Output.Type outputType = Type.PITCH;
    private static final int imageId = R.mipmap.ic_launcher;


    Pitch(int portNumber) {
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
