package org.cmucreatelab.flutter_android.classes.relationships;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Relationship
 *
 * An interface which defines the base type of relationship.
 *
 */
public interface Relationship extends Serializable {

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

    int getRelationshipTypeId();

    int getGreenImageIdLg();

    int getGreenImageIdMd();

    int getGreyImageIdSm();

}
