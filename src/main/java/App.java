import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.Node;
import models.Relationship;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static final Integer NODE_FEATURE_COUNT = 3;
	private Map<Integer, Node> nodeMap;
	private RandEnvGenerator randEnvGen;
	
	public App() {
		nodeMap = new HashMap<>();
		randEnvGen = RandEnvGenerator.getInstance();
	}
	
	public Map<Integer, Node> getNodeMap() {
		return nodeMap;
	}

	public RandEnvGenerator getRandEnvGen() {
		return randEnvGen;
	}

	public static void main(String[] args) {
		logger.info("Initializing app.");
		App app = new App(); 
		app.init();
		app.parseAndGenerate();
		logger.info("Finished parsing json file and generating node relationships.");
	}
	
	public void init() {
		randEnvGen.genManufacturers(10, 4);
		randEnvGen.genOwners(2);
	}
	
	public void parseAndGenerate() {
		JSONParser parser = new JSONParser();
		try {
			FileReader fileReader = new FileReader("sample-nodes.json");
			Object object = parser.parse(fileReader);
			JSONArray array = (JSONArray) object;
			Iterator<JSONObject> iterator = array.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonNode = iterator.next();
				Long longId = (Long) jsonNode.get("id"); //json-simple parses numbers as Longs and not ints...
				int id =  new Integer(longId.intValue());
				JSONArray neighbors = (JSONArray) jsonNode.get("neighbors");
				Iterator<Long> neighborIterator = neighbors.iterator();
				Node mappedNode = createOrGetNode(id);
				formRelationship(mappedNode, neighborIterator);
			}
		} catch (IOException|ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	public Node createOrGetNode(int id) {
		Node node = null;
		if (nodeMap.containsKey(id)) {
			node = nodeMap.get(id);
		} else {
			node = randEnvGen.genRandomizeNode(id, NODE_FEATURE_COUNT);
			nodeMap.put(id,  node);
		}
		return node;
	}
	
	public void formRelationship(Node src, Iterator<Long> neighbors) {
		if (src == null || !neighbors.hasNext()) return;
		while (neighbors.hasNext()) {
			Node neighbor = createOrGetNode(neighbors.next().intValue());
			src.addRelationship(neighbor, Relationship.randomRelationship());
		}
	}
}
