package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 9/1/2016.
 *
 *
 *
 * A class that represents there is no current Relationship set.
 */
public class NoRelationship implements Relationship {

    private static final Relationship.Type relationshipType = Type.NO_RELATIONSHIP;


    public NoRelationship() {}


    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

    @Override
    public int getGreenImageIdLg() {
        return 0;
    }

    @Override
    public int getGreenImageIdMd() {
        return 0;
    }

}
