package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

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
    public static final int greenImageId = R.drawable.relationship_green_change;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

    @Override
    public int getGreenImageId() {
        return greenImageId;
    }

}
