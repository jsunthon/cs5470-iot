package models.nodes;

import models.*;
import models.history.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SocialNode extends Node {
    private static final Logger logger = LoggerFactory.getLogger(SocialNode.class);

    private Set<Edge> sortedEdges;

    private Map<Relationship, LinkedList<Edge>> relationshipMap;
    private History history;

    public static Integer NODE_ID_COUNTER = 0;
    public static Integer DEFAULT_MAX_HISTORY_SIZE = 10;

    public SocialNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        super(id, manufacturer, role, timeToLive);
        relationshipMap = new HashMap<Relationship, LinkedList<Edge>>();
        history = new History(SocialNode.DEFAULT_MAX_HISTORY_SIZE);
        sortedEdges = new TreeSet<>(new NodeEdgeCentrality()); //sorted edges
    }

    public Map<Relationship, LinkedList<Edge>> getRelationshipMap() {
        return relationshipMap;
    }

    public void addRelationship(SocialNode node, Relationship relationship) {
        if (edgeExists(node)) return;
        Edge edge = new Edge(this, node, relationship);
        if (relationshipMap.containsKey(relationship)) {
            relationshipMap.get(relationship).add(edge);
        } else {
            LinkedList<Edge> edges = new LinkedList<>();
            edges.add(edge);
            relationshipMap.put(relationship, edges);
        }
        sortedEdges.add(edge);
    }

    public boolean edgeExists(SocialNode dest) {
        for (Edge edge : sortedEdges) {
            if (edge.getDest().equals(dest)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean edgeExists(int id, int feature, int type) {
    	for (Edge edge : sortedEdges) {
    		if (edge.getRelationship().equals(Relationship.getRelationship(type))) {
        		Node node = edge.getDest();
        		if (node.getId() == id && 
        				node.getFeatures().contains(feature)) {
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
        for (Map.Entry<Relationship, LinkedList<Edge>> entry : relationshipMap.entrySet()) {
            LinkedList<Edge> edges = entry.getValue();
            centrality += edges.size();
        }
        return centrality;
    }

    public boolean hasFeature(Integer feature) {
        return features.contains(feature);
    }
    

	/* ================================================== */

    public Set<Edge> getSortedEdges() {
        return sortedEdges;
    }

    public void setSortedEdges(Set<Edge> sortedEdges) {
        this.sortedEdges = sortedEdges;
    }

    /**
     * Search for node(s) with this feature.
     *
     * @param feature the feature to search for
     * @param limits  limit the search result to this number (e.g. 5)
     * @return Search
     */
    @Override
    public Search discover(Integer feature) {
        /* If this feature was recently searched for and was successfully,
         return  the cached result instead */
        Search recentResult = history.contains(feature);
        if (recentResult != null) {
            return recentResult;
        }

        /* Initialized the search meta data */
        Search search = new Search(feature, System.currentTimeMillis());
        history.push(search);

        /* Keep Track of the queue and the visited nodes */
        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();

        /* Dummy values: add self to the queue (first one) */
        nodeQueue.offer(this);

        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();

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

                /* Additonals nodes that contains the feature
                 * are stored in a seperate varaibles */
                if (search.getNode() == null) {
                    search.setSuccess(current);
                } else {
                    search.addSuccess(current);
                }

                search.addSuccess(current);

                if (search.getNodes().size() == search.getLimit()) {
                    break;
                }
            }

            /* Add the all the relationship on to the queue if it has
            * not been visited before */
            for (Edge edge : current.sortedEdges) {
                if (!visited.contains(edge.getDest())) {
                    nodeQueue.offer(edge.getDest());
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        return search;
    }

    /**
     * Search a node for its id
     */
    public Search discover(int idToSearch) {
        /* If this ID was recently searched for and was successfully,
         return the cached result instead */
        Search recentResult = history.contains(idToSearch);
        if (recentResult != null) {
            return recentResult;
        }

        /* Initialized the search meta data */
        Search search = new Search(id, System.currentTimeMillis());
        history.push(search);

        /* Keep Track of the queue and the visited nodes */
        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();

        /* Dummy values: add self to the queue (first one) */
        nodeQueue.offer(this);

        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();

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
            for (Edge edge : current.sortedEdges) {
                if (!visited.contains(edge.getDest())) {
                    nodeQueue.offer(edge.getDest());
                }
            }
        }
        search.setEnd(System.currentTimeMillis());
        return search;
    }

    private class NodeEdgeCentrality implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            int centralityDest1 = e1.getDest().getCentrality();
            int centralityDest2 = e2.getDest().getCentrality();

            int diff = centralityDest2 - centralityDest1;
            if (diff == 0) {
                /* Use diversity score for tie breaker/ but if the
                * score are equal, pick whatever ... for now */
                if (e2.getDiversityScore() == e1.getDiversityScore()) {
                    return 1;
                } else {
                    return e2.getDiversityScore() - e1.getDiversityScore();
                }
            } else {
                return diff;
            }
        }
    }


    @Override
    public String toString() {
        return "{id:" + id + "}";
    }
}
