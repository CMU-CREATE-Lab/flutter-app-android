package org.cmucreatelab.flutter_android.helpers.guided_input;


import java.util.ArrayList;

/**
 * Created by Steve on 6/7/2016.
 */
public class OptionsNode extends Node{


    private String title;
    private ArrayList<String> options;


    public OptionsNode() {
        options = new ArrayList<>();
    }


    // deep copy
    public OptionsNode(OptionsNode other) {
        super(other);
        if (other.title != null) {
            this.title = new String(other.title);
        }
        if (other.options != null) {
            this.options = new ArrayList<>(other.options);
        }
    }


    public void addOption(String string) {
        if (!options.contains(string)) {
            options.add(string);
        }
    }



    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ArrayList<String> getOptions() {
        return this.options;
    }


}
