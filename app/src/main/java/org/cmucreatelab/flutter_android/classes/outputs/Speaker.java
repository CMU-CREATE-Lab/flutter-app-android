package org.cmucreatelab.flutter_android.classes.outputs;

/**
 * Created by Steve on 6/20/2016.
 *
 * Speaker
 *
 * A class that represents the speaker on a flutter.
 *
 */
public class Speaker implements FlutterOutput {

    public static final String SPEAKER_KEY = "speaker_key";
    private static final int NUMBER_OF_OUTPUTS = 2;

    private int portNumber;
    private Output[] outputs;

    // getters
    public int getPortNumber() { return this.portNumber; }
    public Pitch getPitch() { return (Pitch)this.outputs[0]; }
    public Volume getVolume() { return (Volume)this.outputs[1]; }


    public Speaker(int portNumber) {
        this.portNumber = portNumber;
        this.outputs = new Output[NUMBER_OF_OUTPUTS];
        outputs[0] = new Pitch(portNumber);
        outputs[1] = new Volume(portNumber);
    }


    @Override
    public Output[] getOutputs() {
        return this.outputs;
    }

}
