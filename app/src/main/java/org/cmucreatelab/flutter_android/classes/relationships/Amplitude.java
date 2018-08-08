package org.cmucreatelab.flutter_android.classes.relationships;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 8/11/2016.
 *
 * Amplitude
 *
 * A class that represents an amplitude relationship.
 */
public class Amplitude implements Relationship {

    private static final Relationship.Type relationshipType = Type.AMPLITUDE;
    private static final int relationshipTypeId = R.string.amplitude;
    private static final int greenImageIdLg = R.drawable.relationship_green_amplitude_l_g_68;
    private static final int greenImageIdMd = R.drawable.relationship_green_amplitude_m_d_56;
    private static final int greyImageIdSm = R.drawable.relationship_grey_amplitude_s_m_20;


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


    private static Amplitude classInstance;


    private Amplitude() {
        super();
    }


    public static synchronized Amplitude getInstance() {
        if (classInstance == null)
            classInstance = new Amplitude();
        return classInstance;
    }

}
