package org.cmucreatelab.flutter_android.classes;

import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 8/11/2016.
 *
 * Link
 *
 * A class that handles linking a sensor to an output.
 *
 */
public class Link {

    private Output mOutput;
    private Relationship mRelationship;


    public Link(Output output, Relationship relationship) throws Exception {
        if (!output.isLinked()) {
            this.mOutput = output;
            this.mOutput.setIsLinked(true);
            this.mRelationship = relationship;
            System.out.println("Link created with " + this.mOutput.getClass().getSimpleName());
        } else {
            throw new Exception("Cannot create link, a link already created with the output");
        }
    }


    // getters
    public Output getOutput() { return this.mOutput; }
    public Relationship getRelationship() { return this.mRelationship; }
}
