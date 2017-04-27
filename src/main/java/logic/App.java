package logic;


import models.Search;
import models.nodes.Node;

import java.util.Set;

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

        Search.DEFAULT_TIMEOUT = 5;
        Search.DEFAULT_LIMIT = 3;

        for (String filename : filenames) {

            RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
            randEnvGen.genManufacturers(10, 4);
            randEnvGen.genOwners(2);

            Parser parser = new Parser();
            parser.parseAndGenSocial(directory + filename);
            parser.genCentral();
            parser.genDecentral();
            parser.setupRandomFeatures(10);

            Integer[] randomFeatureArray = parser.getRandomFeatArr();
            Set<Integer> randomIdSet = parser.getRandomNodeIdSet(100);

            // Central test are not insightful thus will be ignored.
//        Topology<Node> centralTest =
//                new Topology<>(parser.getCentralNodes(), "Central", filename);
//         centralTest.start(randomNodeId, randomFeatureArray);

            Topology.DISPLAY_SEARCHES = false;
            Topology<Node> socialTest =
                    new Topology<>(parser.getSocialNodes(), "Social", filename);
            Topology<Node> decentralTest =
                    new Topology<>(parser.getDecentralNodes(), "Decentralized", filename);

            for (Integer randomNodeId : randomIdSet) {
                socialTest.start(randomNodeId, randomFeatureArray);
            }
            socialTest.printResult();

            for (Integer randomNodeId : randomIdSet) {
                decentralTest.start(randomNodeId, randomFeatureArray);
            }
            decentralTest.printResult();
        }
    }
}
