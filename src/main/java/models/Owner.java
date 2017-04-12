package models;

import java.util.ArrayList;
import java.util.List;

import models.nodes.SocialNode;

public class Owner {
    private Integer id;
    private String name;
    private List<SocialNode> nodes;

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

    public List<SocialNode> getNodes() {
        return nodes;
    }


    public void add(SocialNode node) {
        nodes.add(node);
    }

    public void remove(SocialNode node) {
        nodes.remove(node);
    }

    public void share(SocialNode node, boolean share) {
        if (nodes.contains(node)) {
            node.setShare(share);
        }
    }

    public static Integer getIdCounter() {
        return ID_COUNTER;
    }
}
