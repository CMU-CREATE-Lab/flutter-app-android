package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Frequency
 *
 * A class that represents a frequency relationship.
 *
 */
public class Frequency implements Relationship {

    private static final Relationship.Type relationshipType = Type.FREQUENCY;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
