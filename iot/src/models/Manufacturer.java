package models;

import java.util.ArrayList;

public class Manufacturer {
    private Integer id;
    private String name;
    private List<Feature> features;

    public static Integer MANUFACTUER_ID_COUNTER = 0;

    public Manufacturer(String name) {
        id = MANUFACTUER_ID_COUNTER++;
        this.name = name;
    }

    public void addFeature(Feature feature) {
        feature.add(feature);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Node create() {
        return null;
    }
}
