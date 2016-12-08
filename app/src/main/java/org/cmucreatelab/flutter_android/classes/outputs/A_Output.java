package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.settings.Settings;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Output
 *
 * An abstract class that holds a Settings object to represent the link between input and output
 *
 */
// Each output must have their own separate instance of Settings that will eventually be set to this setting.
    // This is done because, if we want to keep the state of the current links that have been made,
    // I check to see if the outputs settings are null.  If I were to make this instance of settings not null,
    // then when trying to make a link, it will show the default options that settings declares, not the gray question marks.
    // It is slightly ugly, but makes some sense.
    // TODO - Eventually I'd like to explore refactoring how I handle the Settings.
public abstract class A_Output implements Output{

    private boolean isLinked;
    private Settings settings;
    private int portNumber;


    public A_Output(int portNumber) {
        this.portNumber = portNumber;
        isLinked = false;
    }

    // getters
    @Override
    public Settings getSettings() {
        return settings;
    }


    @Override
    public int getPortNumber() {
        return portNumber;
    }


    @Override
    public boolean isLinked() {
        return isLinked;
    }


    // setters
    @Override
    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    @Override
    public void setIsLinked(boolean bool, Output output) {
        isLinked = bool;
        if (!isLinked) {
            String type = settings.getType();
            int max = output.getMax();
            int min = output.getMin();
            settings = new Settings(type, max, min);
        }
    }
}
