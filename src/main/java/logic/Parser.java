package logic;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
	
	private Map<Integer, Node> socialMap;
	private Map<Integer, Node> centralMap;
	private Map<Integer, Node> decentralMap;
	private RandEnvGenerator randEnvGen;
	private JSONParser jsonParser;
	
	public Parser() {
		socialMap = new HashMap<>();
		centralMap = new HashMap<>();
		decentralMap = new HashMap<>();
		randEnvGen = RandEnvGenerator.getInstance();
		jsonParser = new JSONParser();
	}

	public void parseAndGenSocial() {
		try {
			FileReader fileReader = new FileReader("social-network.json");
			Object object = jsonParser.parse(fileReader);
			JSONArray array = (JSONArray) object;
			Iterator<JSONObject> iterator = array.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonNode = iterator.next();
				Long longId = (Long) jsonNode.get("id"); //json-simple parses numbers as Longs and not ints...
				int id =  new Integer(longId.intValue());
				JSONArray neighbors = (JSONArray) jsonNode.get("neighbors");
				Iterator<JSONObject> neighborIterator = neighbors.iterator();
				SocialNode mappedNode = createOrGetSocialNode(id);
				formRelationship(mappedNode, neighborIterator);
			}
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void parseAndGenCentral() {
		try {
			FileReader fileReader = new FileReader("central-network.json");
			JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
			Long id = (Long) jsonObject.get("id");
			JSONArray neighbors = (JSONArray) jsonObject.get("neighbors");
			bindMasterSlaves(id.intValue(), neighbors.iterator());
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	//TODO:
	public void parseAndGenDecentral() {
	}
	
	public void bindMasterSlaves(int id, Iterator<Long> slaves) {
		MasterNode master = (MasterNode) randEnvGen.genRandomizeNode(NodeType.MASTER, id, NODE_FEATURE_COUNT);
		centralMap.put(id, master);
		while (slaves.hasNext()) {
			SlaveNode slave = (SlaveNode) randEnvGen.genRandomizeNode(NodeType.SLAVE,
					slaves.next().intValue(), 
					NODE_FEATURE_COUNT);
			centralMap.put(slave.getId(), slave);
			master.addSlaveNode(slave);
		}
	}
	
	public SocialNode createOrGetSocialNode(int id) {
		SocialNode node = null;
		if (socialMap.containsKey(id)) {
			node = (SocialNode) socialMap.get(id);
		} else {
			node = (SocialNode) randEnvGen.genRandomizeNode(NodeType.SOCIAL, id, NODE_FEATURE_COUNT);
			socialMap.put(id,  node);
		}
		return node;
	}
	
	/**
	 * Forms a random relationshhip between src and each of the neighbor in neighbors
	 * @param src
	 * @param neighbors
	 */
	public void formRandomRelationship(SocialNode src, Iterator<Long> neighbors) {
		if (src == null || !neighbors.hasNext()) return;
		while (neighbors.hasNext()) {
			SocialNode neighbor = createOrGetSocialNode(neighbors.next().intValue());
			src.addRelationship(neighbor, Relationship.randomRelationship());
		}
	}
	
	/**
	 * Call this method if each neighbor has a specified relationship in the JSON file.
	 * @param src
	 * @param neighbors
	 */
	public void formRelationship(SocialNode src, Iterator<JSONObject> neighbors) {
		if (src == null || !neighbors.hasNext()) return;
		while (neighbors.hasNext()) {
			JSONObject jsonObject = neighbors.next();
			int id;
			try {
				id = (int) jsonObject.get("id");
			} catch (ClassCastException e) {
				id = ((Long) jsonObject.get("id")).intValue();
			}
			try {
				Relationship relationship = Relationship.valueOf((String) jsonObject.get("relationship"));
				SocialNode neighbor = createOrGetSocialNode(id);
				src.addRelationship(neighbor, relationship);
			} catch (IllegalArgumentException ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	public Map<Integer, Node> getSocialMap() {
		return socialMap;
	}

	public Map<Integer, Node> getCentralMap() {
		return centralMap;
	}

	public Map<Integer, Node> getDecentralMap() {
		return decentralMap;
	}

	public RandEnvGenerator getRandEnvGen() {
		return randEnvGen;
	}
}
