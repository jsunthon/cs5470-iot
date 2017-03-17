package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Node {

    private Integer id;
    private Manufacturer manufacturer;
    private Date manufacturedDate;

    private Owner owner;
    private ArrayList<Feature> features;
    private HashMap<Relationship, ArrayList<Edge>> relationshipMap;
    private boolean share;

    private TimeToLive timeToLive;


    private Role role;

    public Node() {
    }

    public Owner getOwner() {
        return null;
    }

    public void setOwner(Owner owner) {
    }

    public ArrayList<Feature> getFeatures() {
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

    /*==================================================*/

    public Node discover(Feature feature) {
        return null;
    }


    private enum Role {SENDER, RECIEVER, BOTH}

    private enum TimeToLive {LOW, MEDIUM, HIGHj}


}
