package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Output
 *
 * An abstract class that holds a SettingsProportional object to represent the link between input and output
 *
 */
public abstract class Output implements Serializable {

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


    public Output(int min, int max, int portNumber, Flutter flutter) {
        this.portNumber = portNumber;
        isLinked = false;
        SettingsProportional settings = SettingsProportional.newInstance(flutter);
        settings.setOutputMin(min);
        settings.setOutputMax(max);
        this.settings = settings;
    }


    public void setIsLinked(boolean bool, Output output) {
        isLinked = bool;
        if (!isLinked) {
            int min = output.getMin();
            int max = output.getMax();
            SettingsProportional settings = SettingsProportional.newInstance(this.settings);
            settings.setOutputMin(min);
            settings.setOutputMax(max);
            this.settings = settings;
        }
    }


    // abstract methods


    public abstract int getMax();

    public abstract int getMin();

    public abstract int getOutputImageId();

    public abstract Type getOutputType();

    public abstract String getProtocolString();

}
