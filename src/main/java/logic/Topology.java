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
	
	public Topology(T[] nodes) {
		this.nodes = nodes;
		this.searches = new ArrayList<>();
		this.df = new DecimalFormat("0.00");
	}
	
	/**
	 * Performs a certain number of searches from some nodeid as the src
	 * @param searchNumber
	 * @param nodeId
	 * @param feature
	 */
	public void start(int searchNumber, int nodeId, Integer feature) {
		while (searchNumber > 0) {
			T source = nodes[nodeId];
			doSearch(source, feature);
			searchNumber--;
		}
		finish();
	}
	
	public void start(int srcId, int...features) {
		for (int feature: features) {
			doSearch(nodes[srcId], feature);
		}
		finish();
	}
	
	public void finish() {
		for (Search search : searches) {
			int currBandwidth = search.getBandwidth();
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
			totalPerformance += (search.getEnd() - search.getStart());
		}
		avgBand = totalBand / searches.size();
	}
	
	public Search doSearch(T source, Integer feature) {
		if (source == null) System.out.println("src is null");
		Search search = source.discover(feature);
		searches.add(search);
		System.out.println(search);
		System.out.println(search.getNodes());
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
}
