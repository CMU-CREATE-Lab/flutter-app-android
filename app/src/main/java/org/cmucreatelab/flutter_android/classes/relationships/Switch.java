package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 10/21/2016.
 */
public class Switch implements Relationship {


    private static final Relationship.Type relationshipType = Type.SWITCH;
    public static final int greenImageIdLg = R.drawable.relationship_green_switch_l_g_68;
    public static final int greenImageIdMd = R.drawable.relationship_green_switch_m_d_56;


    @Override
    public Type getRelationshipType() {
        return relationshipType;
    }


    @Override
    public int getGreenImageIdLg() {
        return greenImageIdLg;
    }


    @Override
    public int getGreenImageIdMd() {
        return greenImageIdMd;
    }

}
