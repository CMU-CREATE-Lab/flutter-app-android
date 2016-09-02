package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 8/11/2016.
 *
 * Proportional
 *
 * A class that represents a proportional relationship.
 *
 */
public class Proportional implements Relationship {

    private static final Relationship.Type relationshipType = Type.PROPORTIONAL;
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
