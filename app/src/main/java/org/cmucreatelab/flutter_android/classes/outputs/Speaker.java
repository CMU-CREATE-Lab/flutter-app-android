package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.settings.Settings;

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
    private Flutter flutter;

    // getters
    public int getPortNumber() { return this.portNumber; }
    public Pitch getPitch() { return (Pitch)this.outputs[0]; }
    public Volume getVolume() { return (Volume)this.outputs[1]; }


    public Speaker(int portNumber, Flutter flutter) {
        this.portNumber = portNumber;
        this.outputs = new Output[NUMBER_OF_OUTPUTS];
        outputs[0] = new Pitch(portNumber, flutter);
        outputs[1] = new Volume(portNumber, flutter);
        this.flutter = flutter;
    }


    public static Speaker newInstance(Speaker oldInstance) {
        Speaker newInstance = new Speaker(oldInstance.portNumber,oldInstance.flutter);

        // settings (pitch)
        newInstance.getPitch().setSettings(Settings.newInstance(oldInstance.getPitch().getSettings()));
        newInstance.getPitch().setIsLinked(oldInstance.getPitch().isLinked(),oldInstance.getPitch());
        // settings (volume)
        newInstance.getVolume().setSettings(Settings.newInstance(oldInstance.getVolume().getSettings()));
        newInstance.getVolume().setIsLinked(oldInstance.getVolume().isLinked(),oldInstance.getVolume());

        return newInstance;
    }


    @Override
    public Output[] getOutputs() {
        return this.outputs;
    }

}
