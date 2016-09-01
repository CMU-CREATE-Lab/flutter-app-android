package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 8/11/2016.
 *
 * Output
 *
 * An interface defining the various types of outputs.
 *
 */
// TODO - I may make a static image id reference to whatever image is associated with the type of output
public interface Output {

    enum Type {
        LED,
        SERVO,
        SPEAKER
    }

    // getters
    Type getOutputType();
    Sensor getSensor();
    Relationship getRelationship();

    // setters
    void setSensor(Sensor sensor);
    void setRelationship(Relationship relationship);

}
