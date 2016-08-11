package org.cmucreatelab.flutter_android.classes.outputs;

/**
 * Created by Steve on 6/20/2016.
 *
 * Servo
 *
 * A class that represents a servo on the flutter.
 *
 */
public class Servo implements Output {


    private static final Output.Type outputType = Type.SERVO;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;

    private int currentValue;


    public Servo(int currentLocal) {
        this.setCurrentValue(currentLocal);
    }


    public void setCurrentValue(int currentLocal) {
        if (currentLocal >= MINIMUM && currentLocal <= MAXIMUM) {
            this.currentValue = currentLocal;
        }
    }
    public int getCurrentValue() {
        return this.currentValue;
    }


    @Override
    public Type getOutputType() {
        return outputType;
    }
}
