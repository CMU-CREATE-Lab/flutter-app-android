package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 8/11/2016.
 *
 * Proportional
 *
 * A class that represents a proportional relationship.
 */
public class Proportional implements Relationship {

    private static final Relationship.Type relationshipType = Type.PROPORTIONAL;
    private static final int relationshipTypeId = R.string.proportional;
    private static final int greenImageIdLg = R.drawable.relationship_green_proportional_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_proportional_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_proportional_s_m_20;


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


    private static Proportional classInstance;


    private Proportional() {
        super();
    }


    public static synchronized Proportional getInstance() {
        if (classInstance == null)
            classInstance = new Proportional();
        return classInstance;
    }

}
