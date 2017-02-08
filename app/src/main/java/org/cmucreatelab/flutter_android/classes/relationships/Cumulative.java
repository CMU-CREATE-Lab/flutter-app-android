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
    private static final int relationshipTypeId = R.string.cumulative;
    private static final int greenImageIdLg = R.drawable.relationship_green_cumulative_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_cumulative_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_cumulative_s_m_20;


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

}
