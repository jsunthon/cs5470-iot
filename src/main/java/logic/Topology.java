package logic;

import models.Search;
import models.nodes.Node;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Topology<T extends Node> {
    private T[] nodes;
    private List<Search> searches;
    private DecimalFormat df;

    private String title;
    private String filename;

    private int totalBand;
    private double avgBand;
    private long totalPerformance;
    private int successes;
    private int failures;

    public static boolean DISPLAY_SEARCHES = true;

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
        finish();
    }

    public void finish() {
        updateMetrics();
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

    public List<Search> getSearches() {
        return searches;
    }

    public void printResult() {
        updateMetrics();

        System.out.println("Title:" + title);
        System.out.println("Filename:" + filename);
        System.out.println("Searches:" + searches.size());
        System.out.println("avg bandwidth: " + avgBand);
        System.out.println("success rate:" + successRate());
        System.out.println("avg performance:" + performance());

        System.out.println();
    }

    public void updateMetrics() {
        totalBand = 0;
        totalPerformance = 0;
        successes = 0;
        failures = 0;
        avgBand = 0;

        for (Search search : searches) {
            totalBand += search.getTotalBandwidth();
            totalPerformance += search.getTotalTime();
            if (search.isSuccess()) {
                successes++;
            } else {
                failures++;
            }

            if (DISPLAY_SEARCHES) {
                System.out.println(search);
            }
        }
        avgBand = totalBand / searches.size();
    }

    public int getSuccesses() {
        return successes;
    }

    public int getFailures() {
        return failures;
    }
}
