package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.classes.Link;
import org.cmucreatelab.flutter_android.classes.outputs.Output;

import java.util.ArrayList;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Sensor
 *
 * An abstract class that implements the process of linking a sensor with an output.
 *
 */
public abstract class A_Sensor implements Sensor {

    protected ArrayList<Link> mLinks;


    public A_Sensor() {
        this.mLinks = new ArrayList<>();
    }


    @Override
    public void addLink(Link link) {
        mLinks.add(link);
    }


    @Override
    public void clearLink(Output output) {
        mLinks.remove(output);
        output.setIsLinked(false);
    }

    @Override
    public void clearLinks() {
        for (Link link : mLinks) {
            link.getOutput().setIsLinked(false);
        }
        mLinks.clear();
    }

}
