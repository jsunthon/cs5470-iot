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

    public static long DEFAULT_TIMEOUT = 5;

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
    public static int DEFAULT_LIMIT = 3;
    private int limit;

    // The path to the first node discovered
    private Stack<Edge> firstNodePaths;

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

        firstNodePaths = new Stack<>();
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
        return hasTimeOuted();
    }

    public void addAllNodes(List<Node> nodesToAdd) {
        nodes.addAll(nodesToAdd);
    }


    @Override
    public String toString() {
        return "{"
                + "firstNodeTime: " + getFirstNodeTime() + " "
                + "firstNodeLatency:" + getFirstNodeLatency() + " "
                + "totalTime:" + getTotalTime() + ", "
                + "bandwidth:" + bandwidth + ", "
                + "total_bandwidth:" + totalBandwidth + ", "
                + "success:" + success + ", "
                + "feature:" + feature + ", "
                + "node:" + node + ", "
                + "nodes:" + getNodesStringId()
                + "}";
    }

    // Return a string of Search.nodes' id
    private String getNodesStringId() {
        if (nodes == null || nodes.isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes) {
            stringBuilder.append(node.getId());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public void addFirstNodePath(Edge edge) {
        firstNodePaths.push(edge);
    }

    public Stack<Edge> getFirstNodePaths() {
        return firstNodePaths;
    }

    private int getFirstNodeLatency() {
        int latency = 0;

        for (Edge edge : firstNodePaths) {
            latency += edge.getMs();
        }

        // One way, multiply value by 2 to get round-trip
        return latency;
    }

    public void generatePaths(Stack<Edge> paths) {
        if (isSuccess()) {
            Node node = getNode();

            while (!paths.isEmpty()) {
                Edge path = paths.pop();

                if (path.getDest() == node) {
                    addFirstNodePath(path);
                    node = getFirstNodePaths().peek().getSrc();
                }
            }
        }
    }
}
