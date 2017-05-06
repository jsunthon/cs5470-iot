package models.nodes;

import models.Edge;
import models.Manufacturer;
import models.Neighbor;
import models.Role;
import models.Search;
import models.TimeToLive;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MasterNode extends Node {
	private HashMap<Integer, LinkedList<Neighbor>> slaveNeighborsMap;
	
	public MasterNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
		super(id, manufacturer, role, timeToLive);
		slaveNeighborsMap = new HashMap<>();
	}
	
	public void addSlaveNode(SlaveNode slaveNode) {
		for (Integer feature : slaveNode.getFeatures()) {
			LinkedList<Neighbor> slaveNodeNeighbors;
			if (!slaveNeighborsMap.containsKey(feature)) {
				slaveNodeNeighbors = new LinkedList<>();
				slaveNeighborsMap.put(feature, slaveNodeNeighbors);
			} else {
				slaveNodeNeighbors = slaveNeighborsMap.get(feature);
			}
			Neighbor neighbor = new Neighbor(this, slaveNode);
			slaveNodeNeighbors.addLast(neighbor);
		}
	}

	/**
	 * This method is called when a search is called upon the masternode directly
	 */
	@Override
	public Search discover(Integer feature) {
		Stack<Edge> paths = new Stack<>();
		Search search = new Search(feature, System.currentTimeMillis(), true);
		if (this.hasFeature(feature)) {
			searchSuccess(search, this);
		} else {
			searchSlaveNeighbors(feature, search, paths);
		}
		search.setEnd(System.currentTimeMillis());
		search.generatePaths(paths);
		return search;
	}
	
	/**
	 * This method is called by the slave node when the search originates from the slave node.
	 * This is because the search obj and paths originate from slave
	 * @param feature
	 * @param search
	 * @param paths
	 * @return
	 */
	public Search discover(Integer feature, Search search, Stack<Edge> paths) {
		searchSlaveNeighbors(feature, search, paths);
		return search;
	}
	
	public void searchSlaveNeighbors(Integer feature, Search search, Stack<Edge> paths) {
		search.incrBandwidthVisited(this);
		if (slaveNeighborsMap.containsKey(feature)) {
			LinkedList<Neighbor> slaveNodeList = slaveNeighborsMap.get(feature);
			if (slaveNodeList != null && !slaveNodeList.isEmpty()) {
				//this is a success, so add bandwidth
				Neighbor firstSlaveNeighbor = slaveNodeList.getFirst();
				searchSuccess(search, firstSlaveNeighbor.getDest());
				paths.push(firstSlaveNeighbor);
				//for every slave neighbor, mapped to this feature, add it to the search'es
				//list of nodes that has that feature.
				addSlaveNeighborsSuccess(search, slaveNodeList);
			}
		}
	}
	
	public void searchSuccess(Search search, Node node) {
		search.incrBandwidthVisited(node);
		search.setSuccesses(true, node);
	}
	
	public void addSlaveNeighborsSuccess(Search search, LinkedList<Neighbor> slaves) {
		for (Neighbor slave : slaves) {
			search.addSuccess(slave.getDest());
		}
	}

	public HashMap<Integer, LinkedList<Neighbor>> getSlaveNodeMap() {
		return slaveNeighborsMap;
	}

	public void setSlaveNodeMap(HashMap<Integer, LinkedList<Neighbor>> slaveNodeMap) {
		this.slaveNeighborsMap = slaveNodeMap;
	}
	
	public boolean hasSlaveNode(SlaveNode slaveNode) {
		for (Map.Entry<Integer, LinkedList<Neighbor>> entry : slaveNeighborsMap.entrySet()) {
			for (Neighbor slaveNeighbors : entry.getValue()) {
				if (slaveNeighbors.getDest().equals(slaveNode)) {
					return true;
				}
			}
		}
		return false;
	}
}
