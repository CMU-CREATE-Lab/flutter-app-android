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


}
