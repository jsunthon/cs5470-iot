package logic;

import models.Search;
import models.nodes.Node;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Topology<T extends Node> {
    private T[] nodes;
    private int successes;
    private int failures;
    private int minBand = Integer.MAX_VALUE;
    private int maxBand = Integer.MIN_VALUE;
    private int avgBand = 0;
    private int totalBand = 0;
    private long totalPerformance;
    private List<Search> searches;
    private DecimalFormat df;

    private String title;
    private String filename;

    public Topology(T[] nodes, String title, String filename) {
        this.nodes = nodes;
        this.searches = new ArrayList<>();
        this.df = new DecimalFormat("0.00");
        this.title = title;
        this.filename = filename;
    }

    public void start(int srcId, Integer... features) {
        for (int feature : features) {
            doSearch(nodes[srcId], feature);
        }
        finish(srcId);
    }

    public void finish(int srcId) {
        for (Search search : searches) {
            int currBandwidth = search.getTotalBandwidth();
            totalBand += currBandwidth;
            if (currBandwidth <= minBand) {
                minBand = currBandwidth;
            }
            if (currBandwidth >= maxBand) {
                maxBand = currBandwidth;
            }
            if (search.isSuccess()) {
                successes++;
            } else {
                failures++;
            }
            totalPerformance += search.getTotalTime();
        }
        avgBand = totalBand / searches.size();
        printResult(srcId);
    }

    public Search doSearch(T source, Integer feature) {
        if (source == null) System.out.println("src is null");
        Search search = source.discover(feature);
        searches.add(search);
        return search;
    }

    public double successRate() {
        double unformattedRate = ((1.0 * successes) / (successes + failures)) * 100;
        String formattedRate = df.format(unformattedRate);
        return Double.valueOf(formattedRate);
    }

    public double performance() {
        double unformattedPerf = totalPerformance / searches.size();
        String formattedPerf = df.format(unformattedPerf);
        return Double.valueOf(formattedPerf);
    }

    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }

    public int getSuccesses() {
        return successes;
    }

    public int getFailures() {
        return failures;
    }

    public long getTotalPerformance() {
        return totalPerformance;
    }

    public List<Search> getSearches() {
        return searches;
    }

    public void printBandwidth() {
        System.out.println("{min: " + minBand + ", max: " + maxBand + ", avg: " + avgBand + "}");
    }

    public void printResult(int srcId) {
        System.out.println("Title:" + title);
        System.out.println("Filename:" + filename);
        System.out.println("Node ID: " + srcId);
        for (Search search : searches) {
            System.out.println(search);
        }
        System.out.println("avg bandwidth: " + avgBand);
        System.out.println("success rate:" + successRate());
        System.out.println("avg performance:" + performance());

        System.out.println();

    }
}
