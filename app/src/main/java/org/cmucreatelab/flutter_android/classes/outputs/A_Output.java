package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.Settings;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Output
 *
 * An abstract class that holds a Settings object to represent the link between input and output
 *
 */
public abstract class A_Output implements Output{

    private Settings settings;
    private int portNumber;


    public A_Output(int portNumber) {
        this.portNumber = portNumber;
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


    // setters
    @Override
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
