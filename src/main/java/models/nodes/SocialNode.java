package models.nodes;

import models.*;
import models.history.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.relation.Relation;
import java.util.*;

public class SocialNode extends Node {
    private static final Logger logger = LoggerFactory.getLogger(SocialNode.class);
    
    private Set<Edge> sortedEdges = new TreeSet<>(new NodeEdgeCentrality()); //sorted edges
    
    private Map<Relationship, LinkedList<Edge>> relationshipMap;
    private History history;

    public static Integer NODE_ID_COUNTER = 0;
    public static Integer DEFAULT_MAX_HISTORY_SIZE = 10;

    public SocialNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
        super(id, manufacturer, role, timeToLive);
        relationshipMap = new HashMap<Relationship, LinkedList<Edge>>();
        history = new History(SocialNode.DEFAULT_MAX_HISTORY_SIZE);
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

    /**
     * An edge represent a relationship between this node and another Node.
     * And edgeList therefore, is a list of all the relatipnship with this node.
     *
     * @return A list of edge  (relationship to this node) group by the
     * most occuring relatiship. e.g. (B B B B A A A D D C). If there is a
     * tie then it is group  alphabetically. e .g. (X X X X A A A B B B A Z Z C).
     * Within those group, it is sort by Centrality (again) and used diversity as
     * a tie break. (X-4-2, X-3-4, X-3-2, X-1-4, ... )
//     */
//    public List<Edge> getEdgeList() {
//        List<Edge> edgeList = new ArrayList<Edge>();
//        List<Map.Entry<Relationship, TreeSet<Edge>>> list =
//                new LinkedList<Map.Entry<Relationship, TreeSet<Edge>>>(relationshipMap.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<Relationship, TreeSet<Edge>>>() {
//            @Override
//            public int compare(Map.Entry<Relationship, TreeSet<Edge>> o1, Map.Entry<Relationship, TreeSet<Edge>> o2) {
//                int byCentralality = o2.getValue().size() - o1.getValue().size();
//                if (byCentralality == 0) {
//                    return 1;
//                } else {
//                    return byCentralality;
//                }
//            }
//        });
//
//        for (Map.Entry<Relationship, TreeSet<Edge>> entry : list) {
//            edgeList.addAll(entry.getValue());
//        }
//
//        return edgeList;
//    }
       
    public boolean hasFeature(Feature feature) {
        return features.contains(feature);
    }
    
    

	/* ================================================== */

    public Set<Edge> getSortedEdges() {
		return sortedEdges;
	}

	public void setSortedEdges(Set<Edge> sortedEdges) {
		this.sortedEdges = sortedEdges;
	}

	/* TODO: Implement discovery, find friends/relationship */
    @Override
    public Search discover(Feature feature) {
        /* If this feature was recently searched for and was successfully,
         return  the cached result instead */
        Search recentResult = history.contains(feature);
        if (recentResult != null) {
            return recentResult;
        }

        Search search = new Search(feature, System.currentTimeMillis());
        history.push(search);

        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();
        /* Dummy values */
        nodeQueue.offer(this);

        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();
            search.addBandwidth();
            visited.add(current);
            if (current.hasFeature(feature)) {
                search.setSuccess(true);
                search.setSuccess(current);
                break;
            }
            for (Edge edge : sortedEdges) {
                if (visited.contains(edge.getDest())) {
                    /* Do nothing */
                } else {
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
}
