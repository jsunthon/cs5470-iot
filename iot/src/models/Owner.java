package models;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private Integer id;
    private String name;
    private List<Node> nodes;

    public Owner(Integer id, String name) {
        this.id = id;
        this.name = name;
        nodes = new ArrayList<>();
    }

    public void add(Node node) {
        nodes.add(node);
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    public void share(Node node, boolean share) {
        if (node != null) {
            node.setShare(share);
        }
    }
}
