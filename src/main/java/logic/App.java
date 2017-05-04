package logic;


import models.Search;
import models.nodes.Node;

import java.io.*;
import java.util.List;

public class App {
    private String[] filenames20k = {
            "node-20k-feat-2k",
            "node-20k-feat-4k",
            "node-20k-feat-6k",
            "node-20k-feat-8k",
            "node-20k-feat-10k",
    };

    private String[] filenames50k = {
            "node-50k-feat-5k",
            "node-50k-feat-10k",
            "node-50k-feat-15k",
            "node-50k-feat-20k",
            "node-50k-feat-25k",
    };

    private String directory;
    private String resultDirectory;
    private File   file;
    private Parser parser;

    private App() {
        directory = "./src/main/resources/random-networks/";
        resultDirectory = "./src/main/resources/results/searches/";
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    /**
     * Initialize the environment and node topologies from json files.
     */
    public void start() {
        final int NUMBER_OF_NODES    = 1000;
        final int NUMBER_OF_FEATURES = 1000;

        Search.DEFAULT_TIMEOUT = 5;
        Search.DEFAULT_LIMIT = 3;
        Topology.DISPLAY_SEARCHES = false;

        for (String filename : filenames50k) {

            file = new File(resultDirectory + filename + ".csv");
            RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
            randEnvGen.genManufacturers(10, 4);
            randEnvGen.genOwners(2);

            parser = new Parser();
            parser.parseAndGenSocial(directory + (filename + ".json"));
            parser.genCentral();
            parser.genDecentral();
            parser.setupRandomFeatures(NUMBER_OF_FEATURES);

            List<Integer> randomFeatures = parser.getRandomFeatureList();
            List<Integer> randomNodesId  = parser.getRandomNodeIdSet(NUMBER_OF_NODES);

            Topology<Node> socialTest =
                    new Topology<>(parser.getSocialNodes(), "Social", filename);
            Topology<Node> decentralTest =
                    new Topology<>(parser.getDecentralNodes(), "Decentralized", filename);

            for (int i = 0; i < randomFeatures.size(); i++) {
                socialTest.start(randomNodesId.get(i), randomFeatures.get(i));
                decentralTest.start(randomNodesId.get(i), randomFeatures.get(i));
            }

            socialTest.printResult();
            decentralTest.printResult();

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
