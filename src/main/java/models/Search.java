package models;

import models.nodes.Node;

import java.util.LinkedList;
import java.util.List;

public class Search {
    private long start;
    private long end;
    private int bandwidth;
    private boolean success;

    /*  The feature that a Node searched for */
    private Feature feature;

    private int idToSearch;

    /* The result Node from the discovery search.
      Null if search yield no result */
    private Node node;

    /* Keep track of all the nodes searched for, used for debugging. */
    private List<Node> nodeVisited;

    public Search(Feature feature, long start) {
        this.start = start;
        this.success = false;

        this.feature = feature;

        nodeVisited = new LinkedList<>();
    }

    public Search(int idToSearch, long start) {
        this.start = start;
        this.success = false;

        this.idToSearch = idToSearch;
        nodeVisited = new LinkedList<>();
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

    public long getTotalTime() {
        return end - start;
    }

    public void addVisited(Node node) {
        nodeVisited.add(node);
    }

    public List<Node> getNodeVisited() {
        return nodeVisited;
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

    public int getIdToSearch() {
        return idToSearch;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "{totalTime:" + getTotalTime() + ", bandwidth:" + bandwidth
                + ", success:" + success + ", feature:" + feature + ", node:" + node
                + "}";
    }
}
