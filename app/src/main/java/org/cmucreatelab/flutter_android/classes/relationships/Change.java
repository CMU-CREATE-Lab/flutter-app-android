package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Change
 *
 * A class that represents a change relationship.
 *
 */
public class Change implements Relationship {

    private static final Relationship.Type relationshipType = Type.CHANGE;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
