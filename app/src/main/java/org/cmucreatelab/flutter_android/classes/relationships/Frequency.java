package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 8/11/2016.
 *
 * Frequency
 *
 * A class that represents a frequency relationship.
 */
public class Frequency implements Relationship {

    private static final Relationship.Type relationshipType = Type.FREQUENCY;
    private static final int relationshipTypeId = R.string.frequency;
    private static final int greenImageIdLg = R.drawable.relationship_green_frequency_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_frequency_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_frequency_s_m_20;


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


    private static Frequency classInstance;


    private Frequency() {
        super();
    }


    public static synchronized Frequency getInstance() {
        if (classInstance == null)
            classInstance = new Frequency();
        return classInstance;
    }

}
