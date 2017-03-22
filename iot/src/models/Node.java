package models;

import java.util.*;

public class Node {

    private Integer id;
    private Manufacturer manufacturer;
    private Date manufacturedDate;

    private Owner owner;
    private HashSet<Feature> features;
    private HashMap<Relationship, TreeSet<Edge>> relationshipMap;
    private boolean share;

    private TimeToLive timeToLive;

    private Role role;

    public Node() {
    }

    public Node(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {}

    public Owner getOwner() {
        return null;
    }

    public void setOwner(Owner owner) {
    }

    public HashSet<Feature> getFeatures() {
        return null;
    }

    public void addRelationship(Node node, Relationship relationship) {
    }

    public boolean getShare() {
        return false;
    }

    public void setShare(boolean share) {
    }

    public TimeToLive getTimeToLive() {
        return null;
    }

    public Role getRole() {
        return null;
    }

    public Integer getCentrality() {return null;}

    /*==================================================*/

    public Node discover(Feature feature) {
        return null;
    }


    private enum Role {SENDER, RECEIVER, BOTH}

    private enum TimeToLive {LOW, MEDIUM, HIGH}

    private class NodeEdgeCentrality implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return e1.getNode2().getCentrality() - e2.getNode2().getCentrality();
        }
    }
}
