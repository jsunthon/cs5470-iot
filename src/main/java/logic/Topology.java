package logic;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Feature;
import models.Search;
import models.nodes.Node;

public class Topology<T extends Node> {
	private Map<Integer, T> nodeMap;
	private int successes;
	private int failures;
	private int bandwidth;
	private long totalPerformance;
	private List<Search> searches;
	private DecimalFormat df;
	
	public Topology(Map<Integer, T> nodeMap) {
		this.nodeMap = nodeMap;
		this.searches = new ArrayList<>();
		this.df = new DecimalFormat("0.00");
	}
	
	/**
	 * This method should start the test, performing number of searches.
	 * We also need to decide which node should be the source for each of the searches and 
	 * also what feature is to be searched...
	 * @param searchNumber
	 * @param nodeId
	 * @param feature
	 */
	public void start(int searchNumber, int nodeId, Feature feature) {
		while (searchNumber > 0) {
			T source = nodeMap.get(nodeId);
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
	
	public Search doSearch(T source, Feature feature) {
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
