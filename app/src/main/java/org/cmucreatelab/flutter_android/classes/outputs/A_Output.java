package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

import java.util.ArrayList;

/**
 * Created by Steve on 8/11/2016.
 */
public abstract class A_Output implements Output {

    protected ArrayList<Relationship> mRelationships;



    @Override
    public void addRelationship(Relationship relationship) {
        mRelationships.add(relationship);
    }


    @Override
    public void clearRelationships() {
        mRelationships.clear();
    }

}
