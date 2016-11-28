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

    public static final String RELATIONSHIP_KEY = "relationship_key";

    enum Type {
        NO_RELATIONSHIP,
        PROPORTIONAL,
        AMPLITUDE,
        FREQUENCY,
        CUMULATIVE,
        CHANGE,
        SWITCH,
        CONSTANT
    }

    Type getRelationshipType();
    int getGreenImageIdLg();
    int getGreenImageIdMd();
    int getGreyImageIdSm();

}
