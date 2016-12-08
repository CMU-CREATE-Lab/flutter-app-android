package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.settings.Settings;

/**
 * Created by Steve on 8/11/2016.
 *
 * Output
 *
 * An interface defining the various types of outputs.
 *
 */
public interface Output {

    enum Type {
        LED,
        SERVO,
        SPEAKER
    }

    // getters
    Type getOutputType();
    int getOutputImageId();
    Settings getSettings();
    int getPortNumber();
    int getMax();
    int getMin();
    boolean isLinked();

    // setters
    void setSettings(Settings settings);
    void setIsLinked(boolean bool, Output output);

}
