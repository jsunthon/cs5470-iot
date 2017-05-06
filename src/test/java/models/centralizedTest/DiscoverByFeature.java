package models.centralizedTest;

import org.junit.Test;

import junit.framework.Assert;
import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;
import models.nodes.MasterNode;
import models.nodes.NodeType;
import models.nodes.SlaveNode;

public class DiscoverByFeature {
	@Test
	public void testSearchFromSlaveNodeHasFeature() {
		SlaveNode slaveNode = slaveNodeFeature1();
		Search search = slaveNode.discover(1);
		Assert.assertEquals(search.getBandwidth(), 1);
		Assert.assertEquals(search.getFirstNodePaths().size(), 0);
		Assert.assertTrue(search.isSuccess());
	}
	
	@Test
	public void testSearchFromSlaveNodeDoesntButMasterDoes() {
		SlaveNode slaveNode = slaveNodeFeature1();
		MasterNode masterNode = masterNodeFeatures3();
		slaveNode.addNeighbor(masterNode);
		masterNode.addSlaveNode(slaveNode);
		Search search1 = slaveNode.discover(1);
		Assert.assertEquals(search1.getBandwidth(), 1);
		Assert.assertEquals(search1.getFirstNodePaths().size(), 0);
		Assert.assertTrue(search1.isSuccess());
		Search search2 = slaveNode.discover(5);
		Assert.assertEquals(search2.getBandwidth(), 2);
		Assert.assertEquals(search2.getFirstNodePaths().size(), 1);
		Assert.assertTrue(search2.isSuccess());
	}
	
	@Test
	public void testSearchFromSlaveNodeDoesntAndMasterDoesnt() {
		SlaveNode slaveNode = slaveNodeFeature1();
		MasterNode masterNode = masterNodeFeatures3();
		slaveNode.addNeighbor(masterNode);
		masterNode.addSlaveNode(slaveNode);
		Search search = slaveNode.discover(10);
		Assert.assertEquals(search.getBandwidth(), 3);
		Assert.assertEquals(search.getFirstNodePaths().size(), 0);
		Assert.assertFalse(search.isSuccess());
	}
	
	@Test
	public void testSearchFromMasterDoesHave() {
		SlaveNode slaveNode = slaveNodeFeature1();
		MasterNode masterNode = masterNodeFeatures3();
		slaveNode.addNeighbor(masterNode);
		masterNode.addSlaveNode(slaveNode);
		Search search = masterNode.discover(3);
		Assert.assertEquals(search.getBandwidth(), 1);
		Assert.assertEquals(search.getFirstNodePaths().size(), 0);
		Assert.assertTrue(search.isSuccess());
	}
	
	@Test
	public void testSearchFromMasterDoesntHaveSlaveNodeDoes() {
		SlaveNode slaveNode = slaveNodeFeature1();
		MasterNode masterNode = masterNodeFeatures3();
		slaveNode.addNeighbor(masterNode);
		masterNode.addSlaveNode(slaveNode);
		Search search = masterNode.discover(1);
		Assert.assertEquals(search.getBandwidth(), 2);
		Assert.assertEquals(search.getFirstNodePaths().size(), 1);
		Assert.assertTrue(search.isSuccess());
	}
	
	@Test
	public void testSearchFromMasterDoesntHaveSlaveNodeDoesnt() {
		SlaveNode slaveNode = slaveNodeFeature1();
		MasterNode masterNode = masterNodeFeatures3();
		slaveNode.addNeighbor(masterNode);
		masterNode.addSlaveNode(slaveNode);
		Search search = masterNode.discover(10);
		Assert.assertEquals(search.getBandwidth(), 2);
		Assert.assertEquals(search.getFirstNodePaths().size(), 0);
		Assert.assertFalse(search.isSuccess());
	}
	
	public SlaveNode slaveNodeFeature1() {
		SlaveNode slaveNode = getFeatureSlaveNode(2, 1);
		return slaveNode;
	}
	
	public MasterNode masterNodeFeatures3() {
		MasterNode masterNode = getFeatureMasterNode(2, 3, 5, 7);
		return masterNode;
	}
	
    public SlaveNode getFeatureSlaveNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (SlaveNode) mf.create(NodeType.SLAVE, id, Role.BOTH, TimeToLive.HIGH, features);
    }
    
    public MasterNode getFeatureMasterNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (MasterNode) mf.create(NodeType.MASTER, id, Role.BOTH, TimeToLive.HIGH, features);
    }
}
