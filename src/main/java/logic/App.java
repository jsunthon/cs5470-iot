package logic;


import models.nodes.Node;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    /**
     * Initialize the environment and node topologies from json files.
     */
    public void start() {
        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        parser.parseAndGenSocial("./src/main/javascript/node-20k-feat-2000.json");
        parser.genCentral();
        parser.genDecentral();

        parser.setupRandomFeatures(10);
        Integer[] randomFeatureArray = parser.getRandomFeatArr();
        Integer randomNodeId = parser.getRandomNodeId();

        Topology<Node> socialTest = new Topology<>(parser.getSocialNodes(), "Social");
        Topology<Node> centralTest = new Topology<>(parser.getCentralNodes(), "Central");
        Topology<Node> decentralTest = new Topology<>(parser.getDecentralNodes(), "Decentralized");

        socialTest.start(randomNodeId, randomFeatureArray);
        centralTest.start(randomNodeId, randomFeatureArray);
        decentralTest.start(randomNodeId, randomFeatureArray);

    }
}
