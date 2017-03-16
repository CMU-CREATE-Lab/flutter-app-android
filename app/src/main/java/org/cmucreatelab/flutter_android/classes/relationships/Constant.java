package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 10/21/2016.
 */
public class Constant implements Relationship {

    private static final Relationship.Type relationshipType = Type.CONSTANT;
    private static final int relationshipTypeId = R.string.constant;
    private static final int greenImageIdLg = R.drawable.relationship_green_constant_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_constant_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_constant_s_m_20;


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


    private static Constant classInstance;


    private Constant() {
        super();
    }


    public static synchronized Constant getInstance() {
        if (classInstance == null)
            classInstance = new Constant();
        return classInstance;
    }

}
