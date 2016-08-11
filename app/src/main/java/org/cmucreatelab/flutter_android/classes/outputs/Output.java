package org.cmucreatelab.flutter_android.classes.outputs;

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

    Type getOutputType();

    void setIsLinked(boolean isLinked);
    boolean isLinked();

}
