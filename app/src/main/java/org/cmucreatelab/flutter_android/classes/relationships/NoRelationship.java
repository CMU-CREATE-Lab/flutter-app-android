package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 9/1/2016.
 *
 * NoRelationship
 *
 * A class that represents there is no current Relationship set.
 */
public class NoRelationship implements Relationship {

    private static final Relationship.Type relationshipType = Type.NO_RELATIONSHIP;
    private static final int relationshipTypeId = R.string.no_relationship;


    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }


    @Override
    public int getRelationshipTypeId() {
        return relationshipTypeId;
    }


    @Override
    public int getGreenImageIdLg() {
        return 0;
    }


    @Override
    public int getGreenImageIdMd() {
        return 0;
    }


    @Override
    public int getGreyImageIdSm() {
        return 0;
    }


    // Singleton Implementation


    private static NoRelationship classInstance;


    private NoRelationship() {
        super();
    }


    public static synchronized NoRelationship getInstance() {
        if (classInstance == null)
            classInstance = new NoRelationship();
        return classInstance;
    }

}
