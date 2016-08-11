package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Cumulative
 *
 * A class that represents a cumulative relationship.
 *
 */
public class Cumulative implements Relationship {

    private static final Relationship.Type relationshipType = Type.CUMULATIVE;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
