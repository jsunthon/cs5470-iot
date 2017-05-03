package logic;


import models.Search;
import models.nodes.Node;

import java.io.*;
import java.util.List;
import java.util.Set;

public class App {
    private String[] filenames = {
            "node-20k-feat-2k.json",
            "node-20k-feat-4k.json",
            "node-20k-feat-6k.json",
            "node-20k-feat-8k.json",
            "node-20k-feat-10k.json",
    };

    private String directory = "./src/main/javascript/";
    private File file;
    private Parser parser;

    private App() {
    }

    public static void main(String[] args) {
    	DatabaseClient client = DatabaseClient.getInstance();
//        App app = new App();
//        app.start();
    }

    /**
     * Initialize the environment and node topologies from json files.
     */
    public void start() {
        //Get file from resources folder
        file = new File("./src/main/resources/result.csv");

        Search.DEFAULT_TIMEOUT = 5;
        Search.DEFAULT_LIMIT = 3;

        for (String filename : filenames) {

            RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
            randEnvGen.genManufacturers(10, 4);
            randEnvGen.genOwners(2);

            parser = new Parser();
            parser.parseAndGenSocial(directory + filename);
            parser.genCentral();
            parser.genDecentral();
            parser.setupRandomFeatures(1);

            Integer[] randomFeatureArray = parser.getRandomFeatArr();
            Set<Integer> randomIdSet = parser.getRandomNodeIdSet(1000);

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

            // S = social, D = Decentralized
            appendSearchToFile("S", socialTest.getSearches());
            appendSearchToFile("D", decentralTest.getSearches());
        }
    }

    private void appendSearchToFile(String title, List<Search> searches) {
        // Write(append) each SEARCH result onto file
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (Search search : searches) {
                out.print(title + ",");
                out.print(parser.numOfNodes + ",");
                out.print(parser.numOfFeatures + ",");
                out.print(search.getRootNode().getId() + ",");
                out.print(search.getFeature() + ",");
                out.print((search.isSuccess() ? "T" : "F") + ",");
                out.print(search.getBandwidth() + ",");
                out.print(search.getTotalBandwidth() + ",");
                out.print(search.getFirstNodeLatency() + ",");
                out.print(search.getFirstNodePaths().size());
                out.println();
            }

            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
        }
    }
}
