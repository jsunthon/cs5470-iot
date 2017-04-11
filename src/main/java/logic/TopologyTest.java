package logic;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Feature;
import models.Search;
import models.nodes.Node;

public class TopologyTest<T extends Node> {
	private Map<Integer, T> nodeMap;
	private int successes;
	private int failures;
	private int bandwidth;
	private long totalPerformance;
	private List<Search> searches;
	private DecimalFormat df;
	
	public TopologyTest(Map<Integer, T> nodeMap) {
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
	
	private void finish() {
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
	
	private void doSearch(T source, Feature feature) {
		Search search = source.discover(feature);
		searches.add(search);
	}
	
	public int bandwidth() {
		return bandwidth;
	}
	
	public double successRate() {
		double unformattedRate = (successes / (successes + failures)) * 100;
		String formattedRate = df.format(unformattedRate);
		return Double.valueOf(formattedRate);
	}
	
	public double performance() {
		double unformattedPerf = totalPerformance / searches.size();
		String formattedPerf = df.format(unformattedPerf);
		return Double.valueOf(formattedPerf);
	}
}
