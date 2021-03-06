package models.nodes;

import models.*;
import models.history.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SocialNode extends Node {
    private static final Logger logger = LoggerFactory.getLogger(SocialNode.class);

    private Set<Relationship> sortedRelationships;

    private Map<SocialRelationship, LinkedList<Relationship>> relationshipMap;
    private History history;

    public static Integer NODE_ID_COUNTER = 0;
    public static Integer DEFAULT_MAX_HISTORY_SIZE = 100;

    public SocialNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        super(id, manufacturer, role, timeToLive);
        relationshipMap = new HashMap<SocialRelationship, LinkedList<Relationship>>();
        history = new History(SocialNode.DEFAULT_MAX_HISTORY_SIZE);
        sortedRelationships = new TreeSet<>(new NodeEdgeCentrality()); //sorted edges
    }

    public Map<SocialRelationship, LinkedList<Relationship>> getRelationshipMap() {
        return relationshipMap;
    }

    public void addRelationship(SocialNode node, SocialRelationship socialRelationship) {
        if (edgeExists(node)) return;

        Relationship relationship = new Relationship(this, node, socialRelationship);
        if (relationshipMap.containsKey(socialRelationship)) {
            relationshipMap.get(socialRelationship).add(relationship);
        } else {
            LinkedList<Relationship> relationships = new LinkedList<>();
            relationships.add(relationship);
            relationshipMap.put(socialRelationship, relationships);
        }
        sortedRelationships.add(relationship);
    }

    public boolean edgeExists(SocialNode dest) {
        for (Relationship relationship : sortedRelationships) {
            if (relationship.getDest().equals(dest)) {
                return true;
            }
        }
        return false;
    }

    public boolean edgeExists(int id, Set<Integer> features, int type) {
        for (Relationship relationship : sortedRelationships) {
            if (relationship.getSocialRelationship().equals(SocialRelationship.getRelationship(type))) {
                Node node = relationship.getDest();
                if (node.getId() == id &&
                        node.getFeatures().equals(features)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Centrality refers to how "popular" a node is. The more relationship this
     * node has, the higher the centrality is.
     *
     * @return The total number of relationships this Node has.
     */
    public int getCentrality() {
        int centrality = 0;
        for (Map.Entry<SocialRelationship, LinkedList<Relationship>> entry : relationshipMap.entrySet()) {
            LinkedList<Relationship> relationships = entry.getValue();
            centrality += relationships.size();
        }
        return centrality;
    }

    public boolean hasFeature(Integer feature) {
        return features.contains(feature);
    }
    

	/* ================================================== */

    public Set<Relationship> getSortedRelationships() {
        return sortedRelationships;
    }

    public void setSortedRelationships(Set<Relationship> sortedRelationships) {
        this.sortedRelationships = sortedRelationships;
    }

    /**
     * Search for node(s) with this feature.
     *
     * @param feature the feature to search for
     * @return Search
     */
    @Override
    public Search discover(Integer feature) {
        /* If this feature was recently searched for and was successfully,
         return  the cached result instead */
        Search recentResult = history.containsFeature(feature);
        if (recentResult != null) {
            return recentResult;
        }

        /* Initialized the search meta data */
        Search search = new Search(this, feature, System.currentTimeMillis(), true);
        history.push(search);

        /* Keep Track of the queue and the visited nodes */
        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();

        /* Dummy values: add self to the queue (first one) */
        nodeQueue.offer(this);

        // All the edge explored to discover the firstNode.
        // Used later to calculate the latency from this node
        // to the discovered firstNode
        Stack<Edge> paths = new Stack<>();

        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();
            if (search.hasExceedLimit()) {
                break;
            }

            /* Don't check the same node twice! */
            if (visited.contains(current)) {
                continue;
            }

            /* Mark node as visited and update search metadata */
            visited.add(current);
            search.addVisited(current);

            /* First bandwidth deals with finding the first node that
             * contains the feature, total bandwidth deals with finding nodes
             * until the search.limit have been reach. */
            if (!search.isSuccess()) {
                search.addBandwidth();
            }
            search.addTotalBandwidth();


            if (current.hasFeature(feature)) {
                search.setSuccess(true);

                /* Additional nodes that contains the feature
                 * are stored in a separate variables */
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

            /* Add the all the relationship on to the queue if it has
            * not been visited before */
            for (Relationship relationship : current.sortedRelationships) {
                if (search.hasExceedLimit()) {
                    nodeQueue.clear();
                    break;
                }
                if (!visited.contains(relationship.getDest())) {
                    nodeQueue.offer(relationship.getDest());
                    paths.push(relationship);
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        search.generatePaths(paths);
        return search;
    }

    /**
     * Search a node for its id
     */
    public Search discoverById(Integer idToSearch) {
        /* If this ID was recently searched for and was successfully,
         return the cached result instead */
        Search recentResult = history.containsId(idToSearch);
        if (recentResult != null) {
            return recentResult;
        }

        /* Initialized the search meta data */
        Search search = new Search(this, id, System.currentTimeMillis(), false);
        history.push(search);

        /* Keep Track of the queue and the visited nodes */
        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();

        /* Dummy values: add self to the queue (first one) */
        nodeQueue.offer(this);

        // All the edge explored to discover the firstNode.
        // Used later to calculate the latency from this node
        // to the discovered firstNode
        Stack<Edge> paths = new Stack<>();

        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();
            if (search.hasExceedLimit()) {
                break;
            }
            /* Don't check the same node twice! */
            if (visited.contains(current)) {
                continue;
            }

            /* Mark node as visited and update search metadata */
            visited.add(current);
            search.addBandwidth();
            search.addVisited(current);

            if (current.getId() == idToSearch) {
                search.setSuccess(true);
                search.setSuccess(current);
                break;
            }

            /* Add all the relationship on to the queue if it has
            * not been visited before */
            for (Relationship relationship : current.sortedRelationships) {
                if (!visited.contains(relationship.getDest())) {
                    nodeQueue.offer(relationship.getDest());
                    paths.push(relationship);
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        search.generatePaths(paths);
        return search;
    }

    private class NodeEdgeCentrality implements Comparator<Relationship> {
        @Override
        public int compare(Relationship e1, Relationship e2) {
            int clusteringCoefficiency2 = (int) Math.round(e2.getDest().getClusteringCoeffiency() * 100);
            int clusteringCoefficiency1 = (int) Math.round(e1.getDest().getClusteringCoeffiency() * 100);
            int clusteringCoeffiencyDiff = clusteringCoefficiency2 - clusteringCoefficiency1;
            int centralityDiff = e2.getDest().getCentrality() - e1.getDest().getCentrality();
            int diversityDiff = e2.getDiversityScore() - e1.getDiversityScore();

            if (clusteringCoeffiencyDiff == 0) {
                if (centralityDiff == 0) {
                    if (diversityDiff == 0) {
                        return 1;
                    } else {
                        return diversityDiff;
                    }
                } else {
                    return centralityDiff;
                }
            } else {
                return clusteringCoeffiencyDiff;
            }
        }
    }

    // Used after parsing nodes. Hacky solution
    public void resortEdges() {
        Set<Relationship> setRelationships = new TreeSet<>(new NodeEdgeCentrality()); //sorted edges
        setRelationships.addAll(sortedRelationships);
        sortedRelationships = setRelationships;
    }

    /**
     * Returns a value between 0 and 1 that demonstrates the clustering coeffiency
     * of this node. The formula is C = (# of links between neighbors) / centrality ( centrality - 1)
     * for a directed graph
     */
    public double getClusteringCoeffiency() {
        //First, calculate the number of neighbors of this node
        int centrality = this.getCentrality();
        if (centrality == 0) return 0.0;
        int sharedNeighborLinks = 0;
        //get a integer list of unique neighbor ids
        Set<Integer> neighborIds = getNeighborIds();
        // O(n^2). wonder if we could get it to O(n)
        for (Relationship relationship : getSortedRelationships()) {
            SocialNode neighbor = relationship.getDest();
            for (Relationship neighborsOfNeighbor : neighbor.getSortedRelationships()) {
                SocialNode neighborOfNeighbor = neighborsOfNeighbor.getDest();
                if (neighborIds.contains(neighborOfNeighbor.getId())) {
                    sharedNeighborLinks++;
                }
            }
        }
        return sharedNeighborLinks / (1.0 * centrality * (centrality - 1));
    }

    public Set<Integer> getNeighborIds() {
        Set<Integer> neighborIds = new HashSet<>();
        for (Relationship relationship : getSortedRelationships()) {
            SocialNode neighbor = relationship.getDest();
            neighborIds.add(neighbor.getId());
        }
        return neighborIds;
    }
}
