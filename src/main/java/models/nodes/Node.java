package models.nodes;

import models.*;
import models.history.History;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class Node {
    protected int id;
    protected Manufacturer manufacturer;
    protected Date manufacturedDate;
    protected boolean share;
    protected TimeToLive timeToLive;
    protected Role role;
    protected History history;
    protected Owner owner;
    protected Set<Integer> features;

    public Node(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.role = role;
        this.timeToLive = timeToLive;
        manufacturedDate = new Date();
        features = new HashSet<Integer>();
    }

    public abstract Search discover(Integer feature);

    public int getId() {
        return id;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
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

    public Set<Integer> getFeatures() {
        return features;
    }

    public void addFeature(Integer feature) {
        features.add(feature);
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

    @Override
    public String toString() {
        return "{id:" + id + "}";
    }
}
