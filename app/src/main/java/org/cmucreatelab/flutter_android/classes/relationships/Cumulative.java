package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

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
    public static final int imageId = R.mipmap.ic_launcher;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }


    @Override
    public int getRelationshipImageId() {
        return imageId;
    }

}
