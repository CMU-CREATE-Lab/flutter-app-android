package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 9/1/2016.
 */
public class NoRelationship implements Relationship {


    private static final Relationship.Type relationshipType = Type.NO_RELATIONSHIP;
    public static final int imageId = R.mipmap.ic_launcher;


    public NoRelationship() {}


    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
