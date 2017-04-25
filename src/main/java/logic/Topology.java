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
	private int bandwidth;
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
	
	public void finish() {
		for (Search search : searches) {
			bandwidth += search.getBandwidth();
			if (search.isSuccess()) {
				successes++;
			} else {
				failures++;
			}
			totalPerformance += (search.getEnd() - search.getStart());
		}
	}
	
	public Search doSearch(T source, Integer feature) {
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

	public int getBandwidth() {
		return bandwidth;
	}

	public long getTotalPerformance() {
		return totalPerformance;
	}

	public List<Search> getSearches() {
		return searches;
	}
}
