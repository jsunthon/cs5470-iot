package logic;


import models.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Initializing app.");
        App app = new App();
        app.start();
        logger.info("App finished executing.");
    }

    /**
     * Initialize the environment and node topologies from json files.
     */
    public void start() {
        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        parser.parseAndGenSocial("./src/main/javascript/nodes-1.json");
        parser.genCentral();
        parser.genDecentral();
        Topology<Node> socialTest = new Topology<>(parser.getSocialNodes());
        Topology<Node> centralTest = new Topology<>(parser.getCentralNodes());
        Topology<Node> decentralTest = new Topology<>(parser.getDecentralNodes());
        centralTest.start(1, 1, 15);
        decentralTest.start(1, 1, 15);
        socialTest.start(1, 1, 15);

    }
}
