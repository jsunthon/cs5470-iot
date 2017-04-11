package models.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.TopologyTest;
import models.Edge;
import models.Feature;
import models.Manufacturer;
import models.Relationship;
import models.Role;
import models.Search;
import models.TimeToLive;
import models.history.History;

import java.util.*;

public class SocialNode extends Node {
	private static final Logger logger = LoggerFactory.getLogger(SocialNode.class);
	
	private Map<Relationship, TreeSet<Edge>> relationshipMap;
    private History history;
    
    public static Integer NODE_ID_COUNTER = 0;
    public static Integer DEFAULT_MAX_HISTORY_SIZE = 5;

    public SocialNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
    	super(id, manufacturer, role, timeToLive);
        relationshipMap = new HashMap<Relationship, TreeSet<Edge>>();
        history = new History(SocialNode.DEFAULT_MAX_HISTORY_SIZE);
    }

	public Map<Relationship, TreeSet<Edge>> getRelationshipMap() {
		return relationshipMap;
	}

	public void addRelationship(SocialNode node, Relationship relationship) {
		if (edgeExists(node)) return;
		Edge edge = new Edge(this, node, relationship);
		if (relationshipMap.containsKey(relationship)) {
			relationshipMap.get(relationship).add(edge);
		} else {
			Set<Edge> nodeSet = new TreeSet<Edge>(new NodeEdgeCentrality());
			nodeSet.add(edge);
			relationshipMap.put(relationship, (TreeSet<Edge>) nodeSet);
		}
	}
	
	public boolean edgeExists(SocialNode dest) {
		List<Edge> edges = getEdgeList();
		for (Edge edge : edges) {
			if (edge.getDest().equals(dest)) {
				return true;
			}
		}
		return false;
	}

	public Integer getCentrality() {
		Integer centrality = 0;
		for (Map.Entry<Relationship, TreeSet<Edge>> entry : relationshipMap.entrySet()) {
			Set<Edge> edges = entry.getValue();
			centrality += edges.size();
		}
		return centrality;
	}

	private List<Edge> getEdgeList() {
		List<Edge> edgeList = new ArrayList<Edge>();

		for (Map.Entry<Relationship, TreeSet<Edge>> entry : relationshipMap.entrySet()) {
			edgeList.addAll(entry.getValue());
		}

		return edgeList;
	}

	public boolean hasFeature(Feature feature) {
		return features.contains(feature);
	}

	/* ================================================== */

    /* TODO: Implement discovery, find friends/relationship */
	@Override
    public Search discover(Feature feature) {
		Search search = new Search(System.currentTimeMillis());
        Queue<SocialNode> nodeQueue = new LinkedList<SocialNode>();
        Set<SocialNode> visited = new HashSet<SocialNode>();
		/* Dummy values */
        nodeQueue.offer(this);
        
        while (!nodeQueue.isEmpty()) {
            SocialNode current = nodeQueue.poll();
            search.addBandwidth();
            visited.add(current);
            if (current.hasFeature(feature)) {
                history.push(feature, current);
                search.setSuccess(true);
                break;
            }
            List<Edge> edgeList = current.getEdgeList();
            for (Edge edge : edgeList) {
                if (visited.contains(edge.getDest())) {
                    /* Do nothing */
                } else {
                    nodeQueue.offer(edge.getDest());
                }
            }
        }
        history.push(feature, null);
        search.setEnd(System.currentTimeMillis());
        return search;
    }

	private class NodeEdgeCentrality implements Comparator<Edge> {
		@Override
		public int compare(Edge e1, Edge e2) {
			int centralityDest1 = e1.getDest().getCentrality();
			int centralityDest2 = e2.getDest().getCentrality();
			if (centralityDest1 == centralityDest2) {
				return (int) ((long) e1.getDest().getId() - e2.getDest().getId());
			}
			return centralityDest1 - centralityDest2;
		}
	}
}
