package org.cmucreatelab.flutter_android.classes.outputs;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * Speaker
 *
 * A class that represents the speaker on a flutter.
 *
 */
public class Speaker implements Serializable {

    public static final String SPEAKER_KEY = "speaker_key";

    private int portNumber;
    private Pitch pitch;
    private Volume volume;


    public Speaker(int portNumber) {
        this.portNumber = portNumber;
        pitch = new Pitch(portNumber);
        volume = new Volume(portNumber);
    }


    public int getPortNumber() { return this.portNumber; }
    public Pitch getPitch() { return this.pitch; }
    public Volume getVolume() { return this.volume; }


    public void setPitch(Pitch pitch) { this.pitch = pitch; }
    public void setVolume(Volume volume) { this.volume = volume; }

}
