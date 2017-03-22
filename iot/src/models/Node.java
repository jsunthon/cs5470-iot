package models;

import java.util.*;

public class Node {

    private Integer id;
    private Manufacturer manufacturer;
    private Date manufacturedDate;

    private Owner owner;
    private Set<Feature> features;
    private Map<Relationship, TreeSet<Edge>> relationshipMap;
    private boolean share;

    private TimeToLive timeToLive;
    private Role role;

    public static Integer NODE_ID_COUNTER = 0;

    public Node(Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        id = NODE_ID_COUNTER++;
        manufacturer = manufacturer;
        role = role;
        timeToLive = timeToLive;

        manufacturedDate = new Date();
        features = new HashSet<Feature>();
        relationshipMap = new HashMap<Relationship, TreeSet<Edge>>();
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public void addRelationship(Node node, Relationship relationship) {
        if (relationshipMap.containsKey(relationship)) {
            relationshipMap.get(relationship)
                    .add(new Edge(this, node, relationship));
        } else {
            relationshipMap.put(relationship, new TreeSet<Edge>());
        }
    }

    public boolean getShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public TimeToLive getTimeToLive() {
        return timeToLive;
    }

    public Role getRole() {
        return role;
    }

    public Integer getCentrality() {
        for(Map.Entry(Relationship, TreeSet<Edge>>))
    }

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
