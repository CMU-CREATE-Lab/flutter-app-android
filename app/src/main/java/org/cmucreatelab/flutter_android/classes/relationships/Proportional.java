package org.cmucreatelab.flutter_android.classes.relationships;

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



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
