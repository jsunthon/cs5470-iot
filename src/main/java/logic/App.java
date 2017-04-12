package logic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.nodes.Node;
import models.Feature;

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
		Parser parser = new Parser();
		parser.parseAndGenSocial();
		parser.parseAndGenDecentral();
		parser.parseAndGenCentral();
		Topology<Node> socialTest = new Topology<>(parser.getSocialMap());
		Topology<Node> decentralTest = new Topology<>(parser.getDecentralMap());
		Topology<Node> centralTest = new Topology<>(parser.getCentralMap());
		socialTest.start(100, 0, Feature.randomFeature());
//		decentralTest.start(100, 0, Feature.randomFeature());
		centralTest.start(100, 0, Feature.randomFeature());
	}
}
