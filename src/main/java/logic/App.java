package logic;

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

import models.nodes.Node;
import models.nodes.SocialNode;
import models.Feature;
import models.Relationship;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		logger.info("Initializing app.");
		App app = new App(); 
		app.start();
		logger.info("Finished parsing json file and generating node relationships.");
	}
	
	/**
	 * Initialize the environment and node topologies from json files.
	 */
	public void start() {
		RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
		randEnvGen.genManufacturers(10, 4);
		randEnvGen.genOwners(2);
		Parser parser = Parser.getInstance();
		parser.parseAndGenSocial();
		TopologyTest<Node> socialTest = new TopologyTest<>(parser.getSocialMap());
		socialTest.start(100, 0, Feature.randomFeature());
	}
}
