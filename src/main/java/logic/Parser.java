package logic;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Manufacturer;
import models.Owner;
import models.Relationship;
import models.Role;
import models.TimeToLive;
import models.nodes.MasterNode;
import models.nodes.Node;
import models.nodes.NodeType;
import models.nodes.SlaveNode;
import models.nodes.SocialNode;

public class Parser {
	private static final Integer NODE_FEATURE_COUNT = 3;
	private static final Logger logger = LoggerFactory.getLogger(Parser.class);
	
	private Node[] socialNodes;
	private Node[] centralNodes;
	private Node[] decentralNodes;
	private RandEnvGenerator randEnvGen;
	private JSONParser jsonParser;
	
	public Parser() {
		randEnvGen = RandEnvGenerator.getInstance();
		jsonParser = new JSONParser();
	}
	
	/**
	 * Generates the social topology from a json file
	 * @param path
	 */
	public void parseAndGenSocial(String path) {
		try {
			FileReader fileReader = new FileReader(path);
			Object object = jsonParser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) object;
			JSONArray jsonArray = (JSONArray) jsonObject.get("nodes");
			initNodeArrays(jsonArray.size());
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonNode = iterator.next();
				Long longId = (Long) jsonNode.get("id"); //json-simple parses numbers as Longs and not ints...
				int id =  new Integer(longId.intValue());
				JSONArray features = (JSONArray) jsonNode.get("features");
				JSONArray relationships = (JSONArray) jsonNode.get("relationships");
				Iterator<JSONObject> relationsIterator = relationships.iterator();
				socialNodes[id] = genSocialNode(id, features, relationsIterator);
			}
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Returns true if the central topology could be generated from social topology
	 * @return
	 */
	public boolean genCentral() {
		if (socialNodes.length > 1 && socialNodes.length == centralNodes.length) {
			//start at i = 1 since a node's id can only be as low as 1.
			for (int i = 1; i < centralNodes.length; i++) {
				SocialNode socialNode = (SocialNode) socialNodes[i];
				if (i == 1) {
					//node at index 1 will be MasterNode. All nodes at other indexes are slave nodes
					//maybe not the best practice. will have to make sure that we perform
					//checks for casting any node at index 1 in centralNodes to be MasterNode
					centralNodes[i] = (MasterNode) genNodeFromSocial(socialNode, NodeType.MASTER);
				} else {
					//master node always the first node, aka node with id = 1
					MasterNode masterNode = (MasterNode) centralNodes[1];
					SlaveNode slaveNode = (SlaveNode) genNodeFromSocial(socialNode, NodeType.SLAVE);
					centralNodes[i] = slaveNode;
					masterNode.addSlaveNode(slaveNode);
				}
			}
			return true;
		}
		return false;
	}
	
	//TODO: Generate the decentral network from the social network
	public boolean genDecentral() {
		return false;
	}
	
	/**
	 * Created a node of nodeType that shares the same properties as the socialNode
	 * passed in. These properties are id, share, time to live, role, manufacturer, and owner
	 * @param socialNode
	 * @param nodeType
	 * @return
	 */
	public Node genNodeFromSocial(SocialNode socialNode, NodeType nodeType) {
		int id = socialNode.getId();
		TimeToLive ttl = socialNode.getTimeToLive();
		Role role = socialNode.getRole();
		Set<Integer> featureSet = socialNode.getFeatures();
		Integer[] featureArr = featureSet.toArray(new Integer[featureSet.size()]);
		boolean share = socialNode.getShare();
		Owner owner = socialNode.getOwner();
		Manufacturer manuf = socialNode.getManufacturer();
		Node createdNode = null;
		switch (nodeType) {
		case MASTER:
			createdNode = (MasterNode) manuf.create(NodeType.MASTER, id, role, ttl, featureArr);
			break;
		case SLAVE:
			createdNode = (SlaveNode) manuf.create(NodeType.SLAVE, id, role, ttl, featureArr);
			break;
		case DECENTRAL:
			//TODO: implement decentral node
			break;
		}
		if (createdNode != null) {
			createdNode.setOwner(owner);
			createdNode.setShare(share);
		}
		return createdNode;
	}
	
	public void initNodeArrays(int length) {
		socialNodes = new Node[length + 1];
		centralNodes = new Node[length + 1];
		decentralNodes = new Node[length + 1];
	}
	
	/**
	 * Generate a social node with relationships
	 * @param id
	 * @param feature
	 * @param relationships
	 * @return
	 */
	public SocialNode genSocialNode(int id, JSONArray featuresArr, Iterator<JSONObject> relationships) {
		Integer[] features = featureJsonToArr(featuresArr);
		SocialNode srcNode = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, id, features);
		while (relationships.hasNext()) {
			JSONObject jsonNode = relationships.next();
			Long longId = (Long) jsonNode.get("id");
			int intId = longId.intValue();
			JSONArray neighborFeatJson = (JSONArray) jsonNode.get("features");
			Integer[] neighborFeatArr = featureJsonToArr(neighborFeatJson); 
			Long longType = (Long) jsonNode.get("type");
			int intType = longType.intValue();
			SocialNode neighbor = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, intId, neighborFeatArr);
			srcNode.addRelationship(neighbor, Relationship.getRelationship(intType));
		}
		return srcNode;
	}
	
	/**
	 * Gets a node from the list of nodes, creating the node if it doesn't already exist.
	 * Also adds created node to the passed in node array.
	 * @param nodes
	 * @param nodeType
	 * @param id
	 * @param feature
	 * @return
	 */
	public Node getOrCreateNode(Node[] nodes, NodeType nodeType, int id, Integer[] features) {
		Node node = nodes[id];
		if (node == null) {
			node = randEnvGen.genRandomizeNode(nodeType, id, features);
			nodes[id] = node;
		}
		return node;
	}
	
	public Integer[] featureJsonToArr(JSONArray featuresJSON) {
		Integer[] features = new Integer[featuresJSON.size()];
		for (int i = 0; i < featuresJSON.size(); i++) {
			features[i] = ((Long) featuresJSON.get(i)).intValue();
		}
		return features;
	}

//	public void parseAndGenCentral() {
//		try {
//			FileReader fileReader = new FileReader("central-network.json");
//			JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
//			Long id = (Long) jsonObject.get("id");
//			JSONArray neighbors = (JSONArray) jsonObject.get("neighbors");
//			bindMasterSlaves(id.intValue(), neighbors.iterator());
//		} catch (IOException|ParseException e) {
//			logger.error(e.getMessage());
//		}
//	}
	
	
//	public void bindMasterSlaves(int id, Iterator<Long> slaves) {
//		MasterNode master = (MasterNode) randEnvGen.genRandomizeNode(NodeType.MASTER, id, NODE_FEATURE_COUNT);
//		centralMap.put(id, master);
//		while (slaves.hasNext()) {
//			SlaveNode slave = (SlaveNode) randEnvGen.genRandomizeNode(NodeType.SLAVE,
//					slaves.next().intValue(), 
//					NODE_FEATURE_COUNT);
//			centralMap.put(slave.getId(), slave);
//			master.addSlaveNode(slave);
//		}
//	}
//	
//	public SocialNode createOrGetSocialNode(int id) {
//		SocialNode node = null;
//		if (socialMap.containsKey(id)) {
//			node = (SocialNode) socialMap.get(id);
//		} else {
//			node = (SocialNode) randEnvGen.genRandomizeNode(NodeType.SOCIAL, id, NODE_FEATURE_COUNT);
//			socialMap.put(id,  node);
//		}
//		return node;
//	}
	
/*	*//**
	 * Forms a random relationshhip between src and each of the neighbor in neighbors
	 * @param src
	 * @param neighbors
	 *//*
	public void formRandomRelationship(SocialNode src, Iterator<Long> neighbors) {
		if (src == null || !neighbors.hasNext()) return;
		while (neighbors.hasNext()) {
			SocialNode neighbor = createOrGetSocialNode(neighbors.next().intValue());
			src.addRelationship(neighbor, Relationship.randomRelationship());
		}
	}*/
	
	public Node[] getSocialNodes() {
		return socialNodes;
	}

	public Node[] getCentralNodes() {
		return centralNodes;
	}

	public Node[] getDecentralNodes() {
		return decentralNodes;
	}

	/**
	 * Call this method if each neighbor has a specified relationship in the JSON file.
	 * @param src
	 * @param neighbors
	 */
//	public void formRelationship(SocialNode src, Iterator<JSONObject> neighbors) {
//		if (src == null || !neighbors.hasNext()) return;
//		while (neighbors.hasNext()) {
//			JSONObject jsonObject = neighbors.next();
//			int id;
//			try {
//				id = (int) jsonObject.get("id");
//			} catch (ClassCastException e) {
//				id = ((Long) jsonObject.get("id")).intValue();
//			}
//			try {
//				Relationship relationship = Relationship.valueOf((String) jsonObject.get("relationship"));
//				SocialNode neighbor = createOrGetSocialNode(id);
//				src.addRelationship(neighbor, relationship);
//			} catch (IllegalArgumentException ex) {
//				logger.error(ex.getMessage());
//			}
//		}
//	}

	public RandEnvGenerator getRandEnvGen() {
		return randEnvGen;
	}
}
