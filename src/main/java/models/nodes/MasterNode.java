package models.nodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import models.Feature;
import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;

public class MasterNode extends Node {
	private HashMap<Integer, LinkedList<SlaveNode>> slaveNodeMap;
	
	public MasterNode(Integer id, Manufacturer manufacturer, Role role, TimeToLive timeToLive) {
		super(id, manufacturer, role, timeToLive);
		slaveNodeMap = new HashMap<>();
	}
	
	public void addSlaveNode(SlaveNode slaveNode) {
		for (Integer feature : slaveNode.getFeatures()) {
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
	public Search discover(Integer feature) {
		Search search = new Search(feature, System.currentTimeMillis());
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

	public HashMap<Integer, LinkedList<SlaveNode>> getSlaveNodeMap() {
		return slaveNodeMap;
	}

	public void setSlaveNodeMap(HashMap<Integer, LinkedList<SlaveNode>> slaveNodeMap) {
		this.slaveNodeMap = slaveNodeMap;
	}
	
	public boolean hasSlaveNode(SlaveNode slaveNode) {
		for (Map.Entry<Integer, LinkedList<SlaveNode>> entry : slaveNodeMap.entrySet()) {
			for (SlaveNode currentNode : entry.getValue()) {
				if (slaveNode.equals(currentNode)) {
					return true;
				}
			}
		}
		return false;
	}
}
