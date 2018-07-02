package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;

/**
 * Created by Steve on 12/13/2016.
 *
 * Volume
 *
 * A class that represents the volume of the speaker on the flutter.
 *
 */
public class Volume extends Output implements FlutterOutput {

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;
    private static final String TYPE = "v";
    private static final Output.Type outputType = Type.VOLUME;
    private static final int imageId = R.mipmap.ic_launcher;


    Volume(int portNumber, Flutter flutter) {
        super(MAXIMUM, MINIMUM, portNumber, flutter);
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


    @Override
    public Output[] getOutputs() {
        Output[] outputs = {this};
        return outputs;
    }

}
