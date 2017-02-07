package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.settings.Settings;

/**
 * Created by Steve on 8/11/2016.
 *
 * Output
 *
 * An abstract class that holds a Settings object to represent the link between input and output
 *
 */
public abstract class Output {

    enum Type {
        RED_LED,
        GREEN_LED,
        BLUE_LED,
        SERVO,
        PITCH,
        VOLUME
    }

    private boolean isLinked;
    private Settings settings;
    private int portNumber;

    // getters
    public Settings getSettings() { return settings; }
    public int getPortNumber() { return portNumber; }
    public boolean isLinked() { return isLinked; }

    // setters
    public void setSettings(Settings settings) { this.settings = settings; }


    public Output(String type, int max, int min, int portNumber) {
        this.portNumber = portNumber;
        isLinked = false;
        settings = new Settings(type, max, min);
    }


    public void setIsLinked(boolean bool, Output output) {
        isLinked = bool;
        if (!isLinked) {
            String type = settings.getType();
            int max = output.getMax();
            int min = output.getMin();
            settings = new Settings(type, max, min);
        }
    }


    // abstract methods


    public abstract int getMax();

    public abstract int getMin();

    public abstract int getOutputImageId();

    public abstract Type getOutputType();

    public abstract String getProtocolString();

}
