package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Relationship
 *
 * An interface which defines the base type of relationship.
 *
 */
// TODO - I may make a static image id reference to whatever image is associated with the type of relationship
public interface Relationship {

    public static final String RELATIONSHIP_KEY = "relationship_key";

    enum Type {
        NO_RELATIONSHIP,
        PROPORTIONAL,
        AMPLITUDE,
        FREQUENCY,
        CUMULATIVE,
        CHANGE
    }

    Type getRelationshipType();

}
