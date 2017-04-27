package logic;


import models.nodes.Node;

public class App {
    private String[] filenames = {
            "node-20k-feat-1k.json",
            "node-20k-feat-2k.json",
            "node-20k-feat-4k.json",
            "node-20k-feat-6k.json",

            "node-50k-feat-2_5k.json",
            "node-50k-feat-5k.json",
            "node-50k-feat-10k.json",
            "node-50k-feat-15k.json",
    };

    private String directory = "./src/main/javascript/";

    private App() {
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();

    }

    /**
     * Initialize the environment and node topologies from json files.
     */
    public void start() {
        String filename = filenames[0];

        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        parser.parseAndGenSocial(directory + filename);
        parser.genCentral();
        parser.genDecentral();

        parser.setupRandomFeatures(10);
        Integer[] randomFeatureArray = parser.getRandomFeatArr();
        Integer randomNodeId = parser.getRandomNodeId();

        Topology<Node> socialTest =
                new Topology<>(parser.getSocialNodes(), "Social", filename);
        Topology<Node> centralTest =
                new Topology<>(parser.getCentralNodes(), "Central", filename);
        Topology<Node> decentralTest =
                new Topology<>(parser.getDecentralNodes(), "Decentralized", filename);

        socialTest.start(randomNodeId, randomFeatureArray);
        centralTest.start(randomNodeId, randomFeatureArray);
        decentralTest.start(randomNodeId, randomFeatureArray);

    }
}
