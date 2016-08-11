package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 8/11/2016.
 *
 * Output
 *
 * An interface defining the various types of outputs.
 *
 */
public interface Output {

    public enum Type {
        LED,
        SERVO,
        SPEAKER
    }

    void addRelationship(Relationship relationship);
    void clearRelationships();

    Type getOutputType();

}
