package org.cmucreatelab.flutter_android.classes;

/**
 * Created by Steve on 6/20/2016.
 */
public class Servos {


    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;

    private int currentLocal;


    public Servos(int currentLocal) {
        this.setCurrentLocal(currentLocal);
    }


    public void setCurrentLocal(int currentLocal) {
        if (currentLocal >= MINIMUM && currentLocal <= MAXIMUM) {
            this.currentLocal = currentLocal;
        }
    }
    public int getCurrentLocal() {
        return this.currentLocal;
    }
}
