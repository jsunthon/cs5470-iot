package models;

import java.util.ArrayList;

public class Manufacturer {
    private Integer id;
    private String name;
    private ArrayList<Feature> features;

    public static Integer NODE_ID_COUNTER = 0;

    public Manufacturer(Integer id, String name) {}

    public Node create() {
        return null;
    }
}
