package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.classes.Link;

import java.util.ArrayList;

/**
 * Created by Steve on 8/11/2016.
 */
public abstract class A_Sensor implements Sensor {

    protected ArrayList<Link> mLinks;



    @Override
    public void addLink(Link link) {
        mLinks.add(link);
    }


    @Override
    public void clearLinks() {
        mLinks.clear();
    }

}
