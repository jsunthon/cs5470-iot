package models.nodes;

import java.util.Stack;

import models.Edge;
import models.Feature;
import models.Manufacturer;
import models.Neighbor;
import models.Role;
import models.Search;
import models.TimeToLive;

public class SlaveNode extends Node {
	private Neighbor master; //a slave node's neighbor is always the master node
	
	public SlaveNode(Integer id, Manufacturer manufacturer, Role role, 
			TimeToLive timeToLive) {
		super(id, manufacturer, role, timeToLive);
	}
	
	@Override
	public Search discover(Integer feature) {
		Search search = new Search(this, feature, System.currentTimeMillis(), true);
		Stack<Edge> path = new Stack<>();
		search.incrBandwidthVisited(this);
		if (this.hasFeature(feature)) {
			search.setSuccesses(true, this);
			search.addSuccess(this);
		} else {
			path.push(master);
			MasterNode masterNode = (MasterNode) master.getDest();
			if (masterNode.hasFeature(feature)) {
				search.incrBandwidthVisited(masterNode);
				search.setSuccesses(true, masterNode);
				search.addSuccess(masterNode);
			} else {
				masterNode.discover(feature, search, path);
			}
		}
		search.setEnd(System.currentTimeMillis());
		search.generatePaths(path);
		return search;
	}
	
	public Neighbor getMaster() {
		return master;
	}
	
	public void setMaster(Neighbor master) {
		this.master = master;
	}
	
	/**
	 * Adds the masternode as this node's only neighbor
	 * @param neighbor
	 */
	public void addNeighbor(MasterNode master) {
		Neighbor neighbor = new Neighbor(this, master);
		setMaster(neighbor);
	}
}
