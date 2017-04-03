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
        this.manufacturer = manufacturer;
        this.role = role;
        this.timeToLive = timeToLive;

        manufacturedDate = new Date();
        features = new HashSet<Feature>();
        relationshipMap = new HashMap<Relationship, TreeSet<Edge>>();
    }

    public Integer getId() {
        return id;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
    }

    public Map<Relationship, TreeSet<Edge>> getRelationshipMap() {
        return relationshipMap;
    }

    public boolean isShare() {
        return share;
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
            relationshipMap.put(relationship, new TreeSet<Edge>(new NodeEdgeCentrality()));
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
        Integer centrality = 0;
        for (Map.Entry<Relationship, TreeSet<Edge>> entry : relationshipMap.entrySet()) {
            Set<Edge> edges = entry.getValue();
            centrality += edges.size();
        }
        return centrality;
    }

    private List<Edge> getEdgeList() {
        List<Edge> edgeList = new ArrayList<Edge>();

        for (Map.Entry<Relationship, TreeSet<Edge>> entry : relationshipMap.entrySet()) {
            edgeList.addAll(entry.getValue());
        }

        return edgeList;
    }


    public boolean hasFeature(Feature feature) {
        return features.contains(feature);
    }

    /*==================================================*/

    /* TODO: Implement discovery, find friends/relationship */
    public Node discover(Feature feature) {
        Queue<Node> nodeQueue = new LinkedList<Node>();
        Set<Node> visited = new HashSet<Node>();

        /* Dummy values*/
        nodeQueue.offer(this);

        while (!nodeQueue.isEmpty()) {
            Node current = nodeQueue.poll();
            visited.add(current);

            if (current.hasFeature(feature)) {
                return current;
            }
            List<Edge> edgeList = current.getEdgeList();
            for (Edge edge : edgeList) {

                if (visited.contains(edge.getDest())) {
                    /* Do nothing */
                } else {
                    nodeQueue.offer(edge.getDest());
                }
            }
        }
        return null;
    }


    private class NodeEdgeCentrality implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return e1.getDest().getCentrality() - e2.getDest().getCentrality();
        }
    }
}
