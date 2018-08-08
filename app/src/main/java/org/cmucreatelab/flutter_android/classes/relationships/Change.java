package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 8/11/2016.
 *
 * Change
 *
 * A class that represents a change relationship.
 */
public class Change implements Relationship {

    private static final Relationship.Type relationshipType = Type.CHANGE;
    private static final int relationshipTypeId = R.string.change;
    private static final int greenImageIdLg = R.drawable.relationship_green_change_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_change_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_change_s_m_20;


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
        return greenImageIdLg;
    }


    @Override
    public int getGreenImageIdMd() {
        return greenImageIdMd;
    }


    @Override
    public int getGreyImageIdSm() {
        return greyImageIdSm;
    }


    // Singleton Implementation


    private static Change classInstance;


    private Change() {
        super();
    }


    public static synchronized Change getInstance() {
        if (classInstance == null)
            classInstance = new Change();
        return classInstance;
    }

}
