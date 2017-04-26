package models;

import models.nodes.Node;

import java.util.*;

public class Search {
    private long start;
    private long end;

    // The current time when the first node is discovered
    // Subtract this from Search.start to get the total time in ms to discover
    // the first node.
    private long firstNodeTime;

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
    private Set<Node> nodes;

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
            nodes = new HashSet<>();
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

    public long getFirstNodeTime() {
        if (success) {
            return firstNodeTime - start;
        } else {
            return -1;
        }
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
        firstNodeTime = System.currentTimeMillis();
    }

    public void addSuccess(Node successNode) {
        nodes.add(successNode);
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

    public Set<Node> getNodes() {
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
        boolean bandwidthExceeded = totalBandwidth > MAX_BANDWIDTH;
        if (hasTimeOuted() && bandwidthExceeded) {
            failureReason = "timeout and bandwidth exceed";
        } else if (hasTimeOuted()) {
            failureReason = "timeout exceeeded";
        } else if (bandwidthExceeded) {
            failureReason = "bandwidth exceeded";
        }
        return (totalBandwidth > MAX_BANDWIDTH || hasTimeOuted());
    }

    public void addAllNodes(List<Node> nodesToAdd) {
        nodes.addAll(nodesToAdd);
    }


    @Override
    public String toString() {
        if (success) {
            return "{" +
                    "firstNodeTime: " + getFirstNodeTime() + " "
                    + "totalTime:" + getTotalTime() + ", "
                    + "bandwidth:" + bandwidth + ", "
                    + "total_bandwidth:" + totalBandwidth + ", "
                    + "success:" + success + ", "
                    + "feature:" + feature + ", "
                    + "node:" + node + ", "
                    + "nodes:" + getNodesStringId()
                    + "}";
        } else {
            return "{" +
                    "firstNodeTime: " + getFirstNodeTime() + " "
                    + "totalTime:" + getTotalTime() + ", "
                    + "bandwidth:" + bandwidth + ", "
                    + "total_bandwidth:" + totalBandwidth + ", "
                    + "success:" + success + ", "
                    + "feature:" + feature + ", "
                    + "node:" + node + ", "
                    + "nodes:" + getNodesStringId() + ", "
                    + "failReason:" + failureReason
                    + "}";
        }
    }

    // Return a string of Search.nodes' id
    private String getNodesStringId() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Node node : nodes) {
            stringBuilder.append(node.getId());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
