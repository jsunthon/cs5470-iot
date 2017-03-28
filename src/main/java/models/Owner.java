package models;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private Integer id;
    private String name;
    private List<Node> nodes;

    private static Integer ID_COUNTER = 0;

    public Owner(String name) {
        id = ID_COUNTER++;
        this.name = name;

        nodes = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }


    public void add(Node node) {
        nodes.add(node);
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    public void share(Node node, boolean share) {
        if (nodes.contains(node)) {
            node.setShare(share);
        }
    }

    public static Integer getIdCounter() {
        return ID_COUNTER;
    }
}
