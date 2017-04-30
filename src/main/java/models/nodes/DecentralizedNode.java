package models.nodes;

import models.*;
import models.history.History;

import java.util.*;

public class DecentralizedNode extends Node {

    // Each neighbor is an edge where src=this, dest=the neighboring node
    private Set<Neighbor> neighbors;
    private History history;
    public static Integer DEFAULT_MAX_HISTORY_SIZE = 10;

    public DecentralizedNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        super(id, manufacturer, role, timeToLive);
        neighbors = new LinkedHashSet<>();
        history = new History(DEFAULT_MAX_HISTORY_SIZE);
    }

    public void addNeighbor(DecentralizedNode node) {
        neighbors.add(new Neighbor(this, node));
    }

    public boolean hasFeature(Integer feature) {
        return features.contains(feature);
    }

    @Override
    public Search discover(Integer feature) {
        // If this feature was recently searched for and was successfully,
        // return the cached result instead
        Search recentResult = history.containsFeature(feature);
        if (recentResult != null) {
            return recentResult;
        }

        // Initialized the search meta data
        Search search = new Search(this, feature, System.currentTimeMillis(), true);
        history.push(search);

        // Keep Track of the queue and the visited nodes
        Queue<DecentralizedNode> nodeQueue = new LinkedList<>();
        Set<DecentralizedNode> visited = new HashSet<>();

        // Dummy values: add self to the queue (first one)
        nodeQueue.offer(this);

        // All the edge explored to discover the firstNode.
        // Used later to calculate the latency from this node
        // to the discovered firstNode
        Stack<Edge> paths = new Stack<>();

        while (!nodeQueue.isEmpty()) {
            DecentralizedNode current = nodeQueue.poll();
            if (search.hasExceedLimit()) {
                break;
            }
            // Don't check the same node twice!
            if (visited.contains(current)) {
                continue;
            }

            // Mark node as visited and update search metadata
            visited.add(current);
            search.addVisited(current);

            // First bandwidth deals with finding the first node that
            // containsFeature the feature, total bandwidth deals with finding nodes
            // until the search.limit have been reach.
            if (!search.isSuccess()) {
                search.addBandwidth();
            }
            search.addTotalBandwidth();


            if (current.hasFeature(feature)) {
                search.setSuccess(true);

                // Additional nodes that containsFeature the feature
                // are stored in a separate variables
                if (search.getNode() == null) {
                    search.setSuccess(current);
                    search.addSuccess(current);
                } else {
                    search.addSuccess(current);
                }

                if (search.getNodes().size() == search.getLimit()) {
                    break;
                }
            }

            // Add the all the relationship on to the queue if it has
            // not been visited before
            for (Neighbor neighbor : current.neighbors) {
                if (!visited.contains(neighbor.getDest())) {
                    nodeQueue.offer((DecentralizedNode) neighbor.getDest());
                    paths.push(neighbor);
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        search.generatePaths(paths);
        return search;
    }

    public Search discoverById(int idToSearch) {
        // If this ID was recently searched for and was successfully,
        // return the cached result instead
        Search recentResult = history.containsId(idToSearch);
        if (recentResult != null) {
            return recentResult;
        }

        // Initialized the search meta data
        Search search = new Search(this, id, System.currentTimeMillis(), false);
        history.push(search);

        // Keep Track of the queue and the visited nodes
        Queue<DecentralizedNode> nodeQueue = new LinkedList<>();
        Set<DecentralizedNode> visited = new HashSet<>();

        // Dummy values: add self to the queue (first one)
        nodeQueue.offer(this);

        // All the edge explored to discover the node
        // Used later to calculate the latency from this node
        // to the discovered node
        Stack<Edge> paths = new Stack<>();

        while (!nodeQueue.isEmpty()) {
            DecentralizedNode current = nodeQueue.poll();
            if (search.hasExceedLimit()) {
                break;
            }
            // Don't check the same node twice!
            if (visited.contains(current)) {
                continue;
            }

            // Mark node as visited and update search metadata
            visited.add(current);
            search.addBandwidth();
            search.addVisited(current);

            if (current.getId() == idToSearch) {
                search.setSuccess(true);
                search.setSuccess(current);
                break;
            }

            // Add all the relationship on to the queue if it has
            // not been visited before
            for (Neighbor neighbor : current.neighbors) {
                if (search.hasExceedLimit()) {
                    nodeQueue.clear();
                    break;
                }
                if (!visited.contains(neighbor.getDest())) {
                    nodeQueue.offer((DecentralizedNode) neighbor.getDest());
                    paths.push(neighbor);
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        search.generatePaths(paths);
        return search;
    }

    public Set<Neighbor> getNeighbors() {
        return neighbors;
    }
}
