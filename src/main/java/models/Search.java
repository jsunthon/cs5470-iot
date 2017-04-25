package models;

import models.nodes.Node;

import java.util.*;

public class Search {
    private long start;
    private long end;
    private static final long DEFAULT_TIMEOUT = 5;
    private static final int MAX_BANDWIDTH = 500;

    /* Number of nodes contacted to discover the FIRST node*/
    private int bandwidth;

    /* Used for discovery by feature where multiple nodes can be the result. Used
    * to avoid conflicts with existing code. */
    private int totalBandwidth;

    private boolean success;

    /*  The feature that a Node searched for */
    private Integer feature;

    private Integer idToSearch;

    /* Other nodes resulted from discovery by feature which does not include the
     * "first" main node. Used to avoid conflict with existing code */
    private List<Node> nodes;

    /* First discovered node, "main node" */
    private Node node;

    /* Keep track of all the nodes searched for, used for debugging. */
    private List<Node> nodeVisited;

    /* Limit feature search result by this value */
    private static int DEFAULT_LIMIT = 3;
    private int limit;

    private String failureReason = "";


    public Search(Integer search, long start, boolean byFeature) {
        if (byFeature) {
            this.start = start;
            this.success = false;

            this.feature = search;

            nodeVisited = new LinkedList<>();
            nodes = new ArrayList<Node>();
            limit = DEFAULT_LIMIT;
        } else {
            this.start = start;
            this.success = false;

            this.idToSearch = search;
            nodeVisited = new LinkedList<>();
        }
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

    public void addSuccess(Node node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void addBandwidth() {
        bandwidth++;
    }

    public int getTotalBandwidth() {
        return totalBandwidth;
    }

    public void addTotalBandwidth() {
        totalBandwidth++;
    }

    public Integer getFeature() {
        return feature;
    }

    public int getIdToSearch() {
        return idToSearch;
    }

    public Node getNode() {
        return node;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getLimit() {
        return limit;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis() - start;
    }

    public boolean hasTimeOuted() {
        return getCurrentTime() > DEFAULT_TIMEOUT;
    }


    public boolean hasExceedLimit() {
        boolean bandwidthExceeded = bandwidth > MAX_BANDWIDTH;
        if (hasTimeOuted() && bandwidthExceeded) {
            failureReason = "timeout and bandwidth exceed";
        } else if (hasTimeOuted()) {
            failureReason = "timeout exceeeded";
        } else if (bandwidthExceeded) {
            failureReason = "bandwidth exceeded";
        }
        return (bandwidth > MAX_BANDWIDTH || hasTimeOuted());
    }

    public void addAllNodes(List<Node> nodesToAdd) {
        nodes.addAll(nodesToAdd);
    }

    @Override
    public String toString() {
        if (success) {
            return
                    "{totalTime:" + getTotalTime() + ", bandwidth:" + bandwidth
                            + ", success:" + success + ", feature:" + feature + ", node:" + node
                            + "}";
        } else {
            return
                    "{totalTime:" + getTotalTime() + ", bandwidth:" + bandwidth
                            + ", success:" + success + ", feature:" + feature + ", node:" + node
                            + ", failReason: " + failureReason + "}";
        }
    }
}
