package logic;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Relationship;
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

	public void parseAndGenSocial(String path) {
		try {
			FileReader fileReader = new FileReader(path);
			Object object = jsonParser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) object;
			JSONArray jsonArray = (JSONArray) jsonObject.get("NODES");
			initNodeArrays(jsonArray.size());
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonNode = iterator.next();
				Long longId = (Long) jsonNode.get("id"); //json-simple parses numbers as Longs and not ints...
				int id =  new Integer(longId.intValue());
				Long featureLong = (Long) jsonNode.get("feature");
				int featureInt = new Integer(featureLong.intValue()); 
				JSONArray relationships = (JSONArray) jsonNode.get("relationships");
				Iterator<JSONObject> relationsIterator = relationships.iterator();
				socialNodes[id] = genSocialNode(id, featureInt, relationsIterator);
			}
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
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
	public SocialNode genSocialNode(int id, int feature, Iterator<JSONObject> relationships) {
		SocialNode srcNode = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, id, feature);
		while (relationships.hasNext()) {
			JSONObject jsonNode = relationships.next();
			Long longId = (Long) jsonNode.get("id");
			int intId = longId.intValue();
			Long longFeature = (Long) jsonNode.get("feature");
			int intFeature = longFeature.intValue();
			Long longType = (Long) jsonNode.get("type");
			int intType = longType.intValue();
			SocialNode neighbor = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, intId, intFeature);
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
	public Node getOrCreateNode(Node[] nodes, NodeType nodeType, int id, int feature) {
		Node node = nodes[id];
		if (node == null) {
			node = randEnvGen.genRandomizeNode(nodeType, id, feature);
			nodes[id] = node;
		}
		return node;
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
