package org.cmucreatelab.flutter_android.helpers.guided_input;

import java.util.ArrayList;

/**
 * Created by Steve on 6/7/2016.
 */
public class Node {


    protected ArrayList<Node> children;
    private String name;


    public Node() {
        children = new ArrayList<>();
    }


    public void link(Node node) {
        if (!children.contains(node)) {
            children.add(node);
        }
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Node getChild(int index) {
        return children.get(index);
    }

}
