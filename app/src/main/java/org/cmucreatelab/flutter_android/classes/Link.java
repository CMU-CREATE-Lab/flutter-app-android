package org.cmucreatelab.flutter_android.classes;

import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 8/11/2016.
 */
public class Link {

    private Output mOutput;
    private Relationship mRelationship;


    public Link(Output output, Relationship relationship) {
        this.mOutput = output;
        this.mRelationship = relationship;
    }


    // getters/setters

    public Output getOutput() { return this.mOutput; }
    public void setOutput(Output output) { this.mOutput = output;}
    public Relationship getRelationship() { return this.mRelationship; }
    public void setRelationship(Relationship relationship) { this.mRelationship = relationship; }

}
