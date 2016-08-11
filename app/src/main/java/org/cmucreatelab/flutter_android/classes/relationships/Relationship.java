package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Relationship
 *
 * An interface which defines the base type of relationship.
 *
 */
public interface Relationship {

    public enum Type {
        PROPORTIONAL,
        AMPLITUDE,
        FREQUENCY,
        CUMULATIVE,
        CHANGE
    }

    Type getRelationshipType();

}
