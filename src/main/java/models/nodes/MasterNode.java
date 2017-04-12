package models.nodes;

import java.util.HashMap;
import java.util.LinkedList;
import models.Feature;
import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;

public class MasterNode extends Node {
	private HashMap<Feature, LinkedList<SlaveNode>> slaveNodeMap;
	
	public MasterNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
		super(id, manufacturer, role, timeToLive);
		slaveNodeMap = new HashMap<>();
	}
	
	public void addSlaveNode(SlaveNode slaveNode) {
		for (Feature feature : slaveNode.getFeatures()) {
			LinkedList<SlaveNode> slaveNodeList;
			if (!slaveNodeMap.containsKey(feature)) {
				slaveNodeList = new LinkedList<>();
				slaveNodeMap.put(feature, slaveNodeList);
			} else {
				slaveNodeList = slaveNodeMap.get(feature);
			}
			slaveNodeList.addLast(slaveNode);
		}
	}

	@Override
	public Search discover(Feature feature) {
		Search search = new Search(System.currentTimeMillis());
		search.addBandwidth();
		if (slaveNodeMap.containsKey(feature)) {
			LinkedList<SlaveNode> slaveNodeList = slaveNodeMap.get(feature);
			if (slaveNodeList != null && !slaveNodeList.isEmpty()) {
				search.setSuccess(true);
			}
		}
		search.setEnd(System.currentTimeMillis());
		return search;
	}

	public HashMap<Feature, LinkedList<SlaveNode>> getSlaveNodeMap() {
		return slaveNodeMap;
	}

	public void setSlaveNodeMap(HashMap<Feature, LinkedList<SlaveNode>> slaveNodeMap) {
		this.slaveNodeMap = slaveNodeMap;
	}
}
