package org.cmucreatelab.flutter_android.classes.relationships;

/**
 * Created by Steve on 8/11/2016.
 *
 * Amplitude
 *
 * A class that represents an amplitude relationship.
 *
 */
public class Amplitutude implements Relationship {

    private static final Relationship.Type relationshipType = Type.AMPLITUDE;



    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }

}
