package org.cmucreatelab.flutter_android.classes.outputs;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Output
 *
 * An abstract class that defines if an output is linked to a sensor.
 *
 */
public abstract class A_Output implements Output {

    protected boolean isLinked;


    @Override
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }


    @Override
    public boolean isLinked() {
        return this.isLinked;
    }
}
