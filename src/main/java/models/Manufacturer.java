package models;

import models.nodes.*;

import java.util.HashSet;
import java.util.Set;

public class Manufacturer {
    private Integer id;
    private String name;
    private Set<Integer> features;

    private static Integer MANUFACTURER_ID_COUNTER = 0;

    public Manufacturer(String name) {
        id = MANUFACTURER_ID_COUNTER++;
        this.name = name;
        features = new HashSet<Integer>();
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

    public void addFeature(Integer feature) {
        features.add(feature);
    }

    public Set<Integer> getFeatures() {
        return features;
    }

    public Node create(NodeType nodeType, int id, Role role, TimeToLive ttl, Integer... features) {
        Node node = null;
        switch (nodeType) {
            case SOCIAL:
                node = new SocialNode(id, this, role, ttl);
                break;
            case MASTER:
                node = new MasterNode(id, this, role, ttl);
                break;
            case SLAVE:
                node = new SlaveNode(id, this, role, ttl);
                break;
            case DECENTRAL:
                node = new DecentralizedNode(id, this, role, ttl);
                break;
            default:
                break;
        }
        if (node != null) {
            for (Integer feature : features) {
                this.features.add(feature);
                node.addFeature(feature);
            }
        }
        return node;
    }

    public static Integer getIdCounter() {
        return MANUFACTURER_ID_COUNTER;
    }
}
