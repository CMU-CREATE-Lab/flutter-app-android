package org.cmucreatelab.flutter_android.helpers.guided_input;

import java.util.ArrayList;

/**
 * Created by Steve on 6/7/2016.
 */
public class Node {


    protected Node parent;
    protected ArrayList<Node> children;
    private String name;


    public Node() {
        children = new ArrayList<>();
    }


    // deep copy
    public Node(Node other) {
        if (other.parent != null) {
            this.parent = new Node(parent);
        }
        if (other.children != null) {
            this.children = new ArrayList<>(other.children);
        }
        if (other.name != null) {
            this.name = new String(other.name);
        }
    }


    public void link(Node node) {
        children.add(node);
        node.setParent(this);
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private void setParent(Node node) {
        parent = node;
    }
    public Node getParent() {
        return parent;
    }
    public Node getChild(int index) {
        return children.get(index);
    }
    public int getChildCount() {
        return children.size();
    }
    public ArrayList<Node> getChildren() {
        return children;
    }

}
