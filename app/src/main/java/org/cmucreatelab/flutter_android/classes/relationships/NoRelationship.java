package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 9/1/2016.
 */
public class NoRelationship implements Relationship {


    private static final Relationship.Type relationshipType = Type.NO_RELATIONSHIP;


    public NoRelationship() {}


    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
