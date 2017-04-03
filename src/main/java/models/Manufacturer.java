package models;

import java.util.HashSet;
import java.util.Set;

public class Manufacturer {
    private Integer id;
    private String name;
    private Set<Feature> features;

    private static Integer MANUFACTURER_ID_COUNTER = 0;

    public Manufacturer(String name) {
        id = MANUFACTURER_ID_COUNTER++;
        this.name = name;
        features = new HashSet<Feature>();
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

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public Node create(int id, Role role, TimeToLive ttl, Feature... features) {
        Node node = new Node(id, this, role, ttl);
        for (Feature feature : features) {
            this.features.add(feature);
            node.addFeature(feature);
        }
        return node;
    }

    public static Integer getIdCounter() {
        return MANUFACTURER_ID_COUNTER;
    }
}
