package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.Feature;
import models.Search;
import models.nodes.MasterNode;
import models.nodes.Node;
import models.nodes.NodeType;
import models.nodes.SlaveNode;
import org.junit.Assert;
import org.junit.Test;

public class TopologyTest {
	@Test
	public void testFinish() {
		Topology<Node> test = new Topology<>(new HashMap<>());
		List<Search> searches = new ArrayList<>();
		Search search1 = new Search(System.currentTimeMillis());
		search1.setStart(0);
		search1.setEnd(500);
		search1.setSuccess(true);
		search1.setBandwidth(8);
		Search search2 = new Search(System.currentTimeMillis());
		search2.setStart(0);
		search2.setEnd(700);
		search2.setSuccess(false);
		search2.setBandwidth(4);
		Search search3 = new Search(System.currentTimeMillis());
		search3.setStart(0);
		search3.setEnd(150);
		search3.setSuccess(true);
		search3.setBandwidth(2);
		searches.add(search1);
		searches.add(search2);
		searches.add(search3);
		test.setSearches(searches);
		test.finish();
		Assert.assertEquals(14, test.getBandwidth());
		Assert.assertEquals(2, test.getSuccesses());
		Assert.assertEquals(1, test.getFailures());
		Assert.assertEquals(66.67, 0.1, test.successRate());
		Assert.assertEquals(450.00, 0.0, test.performance());
	}
	
	//TODO: doSearch for social and decentralized node
	@Test
	public void testDoSearch() {
		searchCentral();
		searchSocial();
	}
	
	private static void searchSocial() {
		RandEnvGenerator randEnvGenerator = RandEnvGenerator.getInstance();
		randEnvGenerator.reset();
		randEnvGenerator.genManufacturers(5, 5);
		randEnvGenerator.genOwners(3);
	}
	
	private static void searchCentral() {
		RandEnvGenerator randEnvGenerator = RandEnvGenerator.getInstance();
		randEnvGenerator.reset();
		randEnvGenerator.genManufacturers(5, 5);
		randEnvGenerator.genOwners(3);
		MasterNode masterNode = (MasterNode) randEnvGenerator.genRandomizeNode(
				NodeType.MASTER, 0, 3);
		//generate a bunch of social node
		Feature[] feature1 = {Feature.SMART_LIGHTING, Feature.NOISE};
		SlaveNode slaveNode1 = (SlaveNode) randEnvGenerator.genRandomizeNode(
				NodeType.SLAVE, 1, feature1);
		slaveNode1.setMaster(masterNode);
		Feature[] feature2 = {Feature.TRAFFIC_CONGESTION};
		SlaveNode slaveNode2 = (SlaveNode) randEnvGenerator.genRandomizeNode(
				NodeType.SLAVE, 2, feature2);
		slaveNode2.setMaster(masterNode);
		Feature[] feature3 = {Feature.WASTE_MANAGEMENT, Feature.SMART_ROAD};
		SlaveNode slaveNode3 = (SlaveNode) randEnvGenerator.genRandomizeNode(
				NodeType.SLAVE, 3, feature3);
		slaveNode3.setMaster(masterNode);
		Feature[] feature4 = {Feature.ELETRONMAGENTIC_FIELD_LEVELS, Feature.SMARTPHONE_DECTECTION}; 
		SlaveNode slaveNode4 = (SlaveNode) randEnvGenerator.genRandomizeNode(
				NodeType.SLAVE, 4, feature4);
		slaveNode4.setMaster(masterNode);
		Feature[] feature5 = {Feature.SMART_LIGHTING};
		SlaveNode slaveNode5 = (SlaveNode) randEnvGenerator.genRandomizeNode(
				NodeType.SLAVE, 5, feature5);
		slaveNode5.setMaster(masterNode);
		masterNode.addSlaveNode(slaveNode1);
		masterNode.addSlaveNode(slaveNode2);
		masterNode.addSlaveNode(slaveNode3);
		masterNode.addSlaveNode(slaveNode4);
		masterNode.addSlaveNode(slaveNode5);
		Topology<Node> centralized = new Topology(new HashMap<>());
		Search search1 = centralized.doSearch(masterNode, Feature.SMART_LIGHTING);
		Assert.assertNotNull(search1);
		Assert.assertTrue(search1.isSuccess());
		Assert.assertEquals((Integer) 1, (Integer) search1.getBandwidth()); 
		Search search2 = centralized.doSearch(masterNode, Feature.STRCTURAL_HEALTH);
		Assert.assertFalse(search2.isSuccess());
		Search search3 = centralized.doSearch(slaveNode3, Feature.NOISE);
		Assert.assertTrue(search3.isSuccess());
		Assert.assertEquals((Integer) 1, (Integer) search3.getBandwidth());
	}
}
