package models;

import models.nodes.Node;

public class Search {
    private long start;
    private long end;
    private int bandwidth;
    private boolean success;

    /*  The feature that a Node searched for */
    private Feature feature;

    /* The result Node from the discovery search.
      Null if search yield no result */
    private Node node;

    public Search(Feature feature, long start) {
        this.start = start;
        this.success = false;

        this.feature = feature;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSuccess(Node node) {
        this.node = node;
    }

    public void addBandwidth() {
        bandwidth++;
    }

    public Feature getFeature() {
        return feature;
    }

    public Node getNode() {
        return node;
    }

}
