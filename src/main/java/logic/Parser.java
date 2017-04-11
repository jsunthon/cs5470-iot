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
import models.nodes.Node;
import models.nodes.NodeType;
import models.nodes.SocialNode;

public class Parser {
	private static final Integer NODE_FEATURE_COUNT = 3;
	private static final Logger logger = LoggerFactory.getLogger(Parser.class);
	
	private Map<Integer, Node> socialMap;
	private Map<Integer, Node> centralMap;
	private Map<Integer, Node> decentralMap;
	private RandEnvGenerator randEnvGen;
	private JSONParser jsonParser;
	private static Parser parser;
	
	private Parser() {
		socialMap = new HashMap<>();
		centralMap = new HashMap<>();
		decentralMap = new HashMap<>();
		randEnvGen = RandEnvGenerator.getInstance();
		jsonParser = new JSONParser();
	}
	
	public static Parser getInstance() {
		if (parser == null) {
			parser = new Parser();
		}
		return parser;
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
				Iterator<Long> neighborIterator = neighbors.iterator();
				SocialNode mappedNode = createOrGetSocialNode(id);
				formRelationship(mappedNode, neighborIterator);
			}
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void parseAndGenCentral() {
	}
	
	public void parseAndGenDecentral() {
		
	}
	
	private SocialNode createOrGetSocialNode(int id) {
		SocialNode node = null;
		if (socialMap.containsKey(id)) {
			node = (SocialNode) socialMap.get(id);
		} else {
			node = (SocialNode) randEnvGen.genRandomizeNode(NodeType.SOCIAL, id, NODE_FEATURE_COUNT);
			socialMap.put(id,  node);
		}
		return node;
	}
	
	private void formRelationship(SocialNode src, Iterator<Long> neighbors) {
		if (src == null || !neighbors.hasNext()) return;
		while (neighbors.hasNext()) {
			SocialNode neighbor = createOrGetSocialNode(neighbors.next().intValue());
			src.addRelationship(neighbor, Relationship.randomRelationship());
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
