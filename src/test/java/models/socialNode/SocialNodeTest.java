package models.socialNode;

import models.*;
import models.nodes.SocialNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SocialNodeTest {
	@Test
	// Test Centrality when the Node has no relationship
	public void centralityTest1() throws Exception {
		SocialNode sn = getTestSocialNode(0);
		Assert.assertEquals(0, sn.getCentrality());
	}

	@Test
	// Test Centrality when the Node has one relation (that has 0 relationship)
	public void centralityTest2() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn1 = getTestSocialNode(1);
		sn0.addRelationship(sn1, SocialRelationship.FRIEND);

		Assert.assertEquals(1, sn0.getCentrality());
	}

	@Test
	// Test Centrality when the Node has two relation (that has 0 relationship
	// each)
	public void centralityTest3() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn1 = getTestSocialNode(1);
		SocialNode sn2 = getTestSocialNode(2);
		sn0.addRelationship(sn1, SocialRelationship.FRIEND);
		sn0.addRelationship(sn2, SocialRelationship.FRIEND);

		Assert.assertEquals(2, sn0.getCentrality());
	}

	@Test
	// Test Centrality when the Node has two relation (that has 1 relationship
	// each)
	public void centralityTest4() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		SocialNode sn1 = getTestSocialNode(1);
		SocialNode sn2 = getTestSocialNode(2);
		sn1.addRelationship(sn2, SocialRelationship.FAMILY);

		SocialNode sn3 = getTestSocialNode(3);
		SocialNode sn4 = getTestSocialNode(4);
		sn3.addRelationship(sn4, SocialRelationship.FRIEND);

		sn0.addRelationship(sn1, SocialRelationship.FRIEND);
		sn0.addRelationship(sn4, SocialRelationship.FAMILY);

		Assert.assertEquals(2, sn0.getCentrality());
	}

	@Test
	// Test Centrality when the Node has two relation (that has 1 relationship
	// each)
	public void centralityTest5() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		SocialNode sn1 = getTestSocialNode(1);
		SocialNode sn2 = getTestSocialNode(2);
		sn1.addRelationship(sn2, SocialRelationship.FRIEND);

		SocialNode sn3 = getTestSocialNode(3);
		SocialNode sn4 = getTestSocialNode(4);
		sn3.addRelationship(sn4, SocialRelationship.FRIEND);

		sn0.addRelationship(sn1, SocialRelationship.FRIEND);
		sn0.addRelationship(sn4, SocialRelationship.FAMILY);

		Assert.assertEquals(2, sn0.getCentrality());
	}

	@Test
	// Test centrality when the node has multiple relationship, each with
	// multiple relationship
	public void centralityTest6() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		SocialNode sn1 = getTestSocialNode(1);
		SocialNode sn2 = getTestSocialNode(2);
		SocialNode sn3 = getTestSocialNode(3);
		sn1.addRelationship(sn2, SocialRelationship.FRIEND);
		sn1.addRelationship(sn3, SocialRelationship.FAMILY);

		SocialNode sn4 = getTestSocialNode(4);
		SocialNode sn5 = getTestSocialNode(5);
		SocialNode sn6 = getTestSocialNode(5);
		sn4.addRelationship(sn5, SocialRelationship.FRIEND);
		sn4.addRelationship(sn6, SocialRelationship.FRIEND);

		sn0.addRelationship(sn1, SocialRelationship.FRIEND);
		sn0.addRelationship(sn4, SocialRelationship.FRIEND);

		Assert.assertEquals("should be 2", 2, sn0.getCentrality());
	}

	@Test
	/*
	 * Test if sorted edges is sorted by centrality, diversity (tie breaker), If
	 * both are tied, whichever relationship get added first is used as the
	 * final tiebreaker
	 */
	public void sortedEdgesTest1() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		// Two relationships
		SocialNode sn1 = node1With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);
		SocialNode sn4 = node4With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);

		// Three relationships
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);

		// Four relationships
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);

		sn0.addRelationship(sn1, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn4, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		sn0.addRelationship(sn15, SocialRelationship.FRIEND);
		sn0.addRelationship(sn20, SocialRelationship.FRIEND);

		TreeSet<Relationship> relationships = (TreeSet<Relationship>) sn0.getSortedRelationships();
		List<Relationship> list = new ArrayList<>(relationships);

		Assert.assertEquals(15, list.get(0).getDest().getId());
		Assert.assertEquals(20, list.get(1).getDest().getId());
		Assert.assertEquals(7, list.get(2).getDest().getId());
		Assert.assertEquals(11, list.get(3).getDest().getId());
		Assert.assertEquals(1, list.get(4).getDest().getId());
		Assert.assertEquals(4, list.get(5).getDest().getId());

		System.out.println(relationships);
	}

	@Test
	/*
	 * Test if sorted edges is sorted by centrality, diversity (tie breaker), If
	 * both are tied, whichever relationship get added first is used as the
	 * final tiebreaker
	 */
	public void sortedEdgesTest2() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		// Two relationships
		SocialNode sn1 = node1With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);
		SocialNode sn4 = node4With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);

		// Three relationships
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);

		// Four relationships
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);

		sn0.addRelationship(sn1, SocialRelationship.CO_WORKER); // 2
		sn0.addRelationship(sn20, SocialRelationship.FRIEND); // 4

		sn0.addRelationship(sn15, SocialRelationship.FRIEND); // 4
		sn0.addRelationship(sn11, SocialRelationship.FRIEND); // 3

		sn0.addRelationship(sn7, SocialRelationship.FAMILY); // 3
		sn0.addRelationship(sn4, SocialRelationship.ACQUAINTANCE); // 2

		TreeSet<Relationship> relationships = (TreeSet<Relationship>) sn0.getSortedRelationships();
		List<Relationship> list = new ArrayList<>(relationships);

		Assert.assertEquals(15, list.get(0).getDest().getId());
		Assert.assertEquals(20, list.get(1).getDest().getId());
		Assert.assertEquals(11, list.get(2).getDest().getId());
		Assert.assertEquals(7, list.get(3).getDest().getId());
		Assert.assertEquals(1, list.get(4).getDest().getId());
		Assert.assertEquals(4, list.get(5).getDest().getId());

		System.out.println(relationships);
	}

	@Test
	/*
	 * Test if sorted edges is sorted by centrality, diversity, clustering
	 * coeffiency. If all three are tied, whichever relationship get added first
	 * is used as the final tiebreaker
	 */
	public void sortedEdgesTest2WithClusteringCoeffiency() throws Exception {
		SocialNode sn0 = getTestSocialNode(0);

		// Two relationships
		SocialNode sn1 = node1With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);
		SocialNode sn4 = node4With2Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.FAMILY);

		// Three relationships
		// sn7 and sn11 have equal centrality, diversity, and clustering coeffiency so whichever
		// is added first to sn0 will be  put first.
		SocialNode sn7 = node7With3Relationship66ClusteringCoeff(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship66ClusteringCoeff(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);

		// Four relationships
		SocialNode sn15 = node15With4Relationships50ClusteringCoeff(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship25ClusteringCoeff(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);

		// sn15 has a higher clustering coeffienct than sn20, so it will be
		sn15.addRelationship(sn11, SocialRelationship.CO_WORKER);
		sn15.addRelationship(sn4, SocialRelationship.ACQUAINTANCE);

		sn20.addRelationship(sn4, SocialRelationship.FAMILY);

		sn0.addRelationship(sn1, SocialRelationship.CO_WORKER); // 2
		sn0.addRelationship(sn20, SocialRelationship.FRIEND); // 4

		sn0.addRelationship(sn15, SocialRelationship.FRIEND); // 4
		sn0.addRelationship(sn11, SocialRelationship.FRIEND); // 3

		sn0.addRelationship(sn7, SocialRelationship.FAMILY); // 3
		sn0.addRelationship(sn4, SocialRelationship.ACQUAINTANCE); // 2

		TreeSet<Relationship> relationships = (TreeSet<Relationship>) sn0.getSortedRelationships();
		List<Relationship> list = new ArrayList<>(relationships);

		Assert.assertEquals(11, list.get(0).getDest().getId());
		Assert.assertEquals(7, list.get(1).getDest().getId());
		Assert.assertEquals(15, list.get(2).getDest().getId());
		Assert.assertEquals(20, list.get(3).getDest().getId());
		Assert.assertEquals(1, list.get(4).getDest().getId());
		Assert.assertEquals(4, list.get(5).getDest().getId());
	}

	@Test
	/**
	 * Test clustering coeffiency when all the neighbors of some node do not
	 * themselves share a link, yielding the smallest possible clustering
	 * coeffiency = 0
	 */
	public void testGetClusteringCoeffiencyNoNeighborLinks() {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		sn0.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn20, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		Assert.assertEquals(0, sn0.getClusteringCoeffiency(), 0.0);
	}

	@Test
	/**
	 * Test clustering coeffiency when each neighbhr of a node is connected to
	 * every other neighbor. This yields the maximal clustering coeffiency = 1
	 */
	public void testGetClusteringCoeffiencyAllNeighborLinks() {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);

		sn15.addRelationship(sn20, SocialRelationship.ACQUAINTANCE);
		sn15.addRelationship(sn7, SocialRelationship.FAMILY);
		sn15.addRelationship(sn11, SocialRelationship.CO_WORKER);

		sn20.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn20.addRelationship(sn7, SocialRelationship.CO_WORKER);
		sn20.addRelationship(sn11, SocialRelationship.FAMILY);

		sn7.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn7.addRelationship(sn11, SocialRelationship.CO_WORKER);
		sn7.addRelationship(sn20, SocialRelationship.FAMILY);

		sn11.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn11.addRelationship(sn20, SocialRelationship.FAMILY);
		sn11.addRelationship(sn7, SocialRelationship.CO_WORKER);

		sn0.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn20, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		Assert.assertEquals(1, sn0.getClusteringCoeffiency(), 0.0);
	}

	@Test
	/**
	 * Test clustering coeffiency when there are common neighbor links such that
	 * clustering coeff = .25
	 */
	public void testGetClusteringCoeffiency25Percent() {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		sn20.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn20.addRelationship(sn7, SocialRelationship.CO_WORKER);
		sn20.addRelationship(sn11, SocialRelationship.FAMILY);

		sn0.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn20, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		Assert.assertEquals(.25, sn0.getClusteringCoeffiency(), 0.0);
	}

	@Test
	/**
	 * Test clustering coeffiency when there are common neighbor links such that
	 * clustering coeff = .50
	 */
	public void testGetClusteringCoeffiency50Percent() {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		sn20.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn20.addRelationship(sn7, SocialRelationship.CO_WORKER);
		sn20.addRelationship(sn11, SocialRelationship.FAMILY);

		sn7.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn7.addRelationship(sn11, SocialRelationship.CO_WORKER);
		sn7.addRelationship(sn20, SocialRelationship.FAMILY);

		sn0.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn20, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		Assert.assertEquals(.50, sn0.getClusteringCoeffiency(), 0.0);
	}
	
	@Test
	public void testGetClusteringCoefficiency75Percent() {
		SocialNode sn0 = getTestSocialNode(0);
		SocialNode sn15 = node15With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY, SocialRelationship.FRIEND);
		SocialNode sn20 = node20With4Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.ACQUAINTANCE,
		        SocialRelationship.CO_WORKER, SocialRelationship.FAMILY);
		SocialNode sn7 = node7With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		SocialNode sn11 = node11With3Relationship(SocialRelationship.ACQUAINTANCE, SocialRelationship.CO_WORKER,
		        SocialRelationship.FAMILY);
		sn20.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn20.addRelationship(sn7, SocialRelationship.CO_WORKER);
		sn20.addRelationship(sn11, SocialRelationship.FAMILY);

		sn7.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn7.addRelationship(sn11, SocialRelationship.CO_WORKER);
		sn7.addRelationship(sn20, SocialRelationship.FAMILY);
		
		sn11.addRelationship(sn15, SocialRelationship.CO_WORKER);
		sn11.addRelationship(sn7, SocialRelationship.ACQUAINTANCE);
		sn11.addRelationship(sn20, SocialRelationship.FAMILY);

		sn0.addRelationship(sn15, SocialRelationship.ACQUAINTANCE);
		sn0.addRelationship(sn20, SocialRelationship.CO_WORKER);
		sn0.addRelationship(sn7, SocialRelationship.FAMILY);
		sn0.addRelationship(sn11, SocialRelationship.FRIEND);
		Assert.assertEquals(.75, sn0.getClusteringCoeffiency(), 0.0);
	}

	@Test
	/**
	 * Test the clustering coeffiency when a node is connected to no other nodes
	 */
	public void testGetClusteringCoeffiency0Centrality() {
		SocialNode sn0 = getTestSocialNode(0);
		Assert.assertEquals(0.0, sn0.getClusteringCoeffiency(), 0.0);
	}

	/* ******************************************* */
	/* *************** SET UP DATA *************** */
	/* ******************************************* */

	public SocialNode getTestSocialNode(int id) {
		return new SocialNode(id, getTestManufacturer(), Role.BOTH, TimeToLive.HIGH);
	}

	public Manufacturer getTestManufacturer() {
		Manufacturer mf = new Manufacturer("test-mf");
		mf.addFeature(1);
		return mf;
	}

	public SocialNode node1With2Relationship(SocialRelationship r1, SocialRelationship r2) {
		SocialNode sn1 = getTestSocialNode(1);

		SocialNode sn2 = getTestSocialNode(2);
		SocialNode sn3 = getTestSocialNode(3);

		sn1.addRelationship(sn2, r1);
		sn1.addRelationship(sn3, r2);

		return sn1;
	}

	public SocialNode node4With2Relationship(SocialRelationship r1, SocialRelationship r2) {
		SocialNode sn4 = getTestSocialNode(4);

		SocialNode sn5 = getTestSocialNode(5);
		SocialNode sn6 = getTestSocialNode(6);

		sn4.addRelationship(sn5, r1);
		sn4.addRelationship(sn6, r2);

		return sn4;
	}

	public SocialNode node7With3Relationship(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3) {
		SocialNode sn7 = getTestSocialNode(7);

		SocialNode sn8 = getTestSocialNode(8);
		SocialNode sn9 = getTestSocialNode(9);
		SocialNode sn10 = getTestSocialNode(10);

		sn7.addRelationship(sn8, r1);
		sn7.addRelationship(sn9, r2);
		sn7.addRelationship(sn10, r3);

		return sn7;
	}
	
	public SocialNode node7With3Relationship66ClusteringCoeff(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3) {
		SocialNode sn7 = getTestSocialNode(7);

		SocialNode sn8 = getTestSocialNode(8);
		SocialNode sn9 = getTestSocialNode(9);
		SocialNode sn10 = getTestSocialNode(10);
		
		sn8.addRelationship(sn9, SocialRelationship.FAMILY);
		sn8.addRelationship(sn10, SocialRelationship.ACQUAINTANCE);
		
		sn9.addRelationship(sn10, SocialRelationship.CO_WORKER);
		sn9.addRelationship(sn8, SocialRelationship.CO_WORKER);

		sn7.addRelationship(sn8, r1);
		sn7.addRelationship(sn9, r2);
		sn7.addRelationship(sn10, r3);

		return sn7;
	}

	public SocialNode node11With3Relationship(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3) {
		SocialNode sn11 = getTestSocialNode(11);

		SocialNode sn12 = getTestSocialNode(12);
		SocialNode sn13 = getTestSocialNode(13);
		SocialNode sn14 = getTestSocialNode(14);

		sn11.addRelationship(sn12, r1);
		sn11.addRelationship(sn13, r2);
		sn11.addRelationship(sn14, r3);

		return sn11;
	}
	
	public SocialNode node11With3Relationship66ClusteringCoeff(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3) {
		SocialNode sn11 = getTestSocialNode(11);

		SocialNode sn12 = getTestSocialNode(12);
		SocialNode sn13 = getTestSocialNode(13);
		SocialNode sn14 = getTestSocialNode(14);
		
		sn12.addRelationship(sn13, SocialRelationship.FAMILY);
		sn12.addRelationship(sn14, SocialRelationship.ACQUAINTANCE);
		
		sn14.addRelationship(sn13, SocialRelationship.FRIEND);
		sn14.addRelationship(sn12, SocialRelationship.CO_WORKER);
		
		sn11.addRelationship(sn12, r1);
		sn11.addRelationship(sn13, r2);
		sn11.addRelationship(sn14, r3);

		return sn11;
	}

	public SocialNode node15With4Relationship(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3,
	        SocialRelationship r4) {

		SocialNode sn15 = getTestSocialNode(15);

		SocialNode sn16 = getTestSocialNode(16);
		SocialNode sn17 = getTestSocialNode(17);
		SocialNode sn18 = getTestSocialNode(18);
		SocialNode sn19 = getTestSocialNode(19);

		sn15.addRelationship(sn16, r1);
		sn15.addRelationship(sn17, r2);
		sn15.addRelationship(sn18, r3);
		sn15.addRelationship(sn19, r4);

		return sn15;
	}

	public SocialNode node15With4Relationships50ClusteringCoeff(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3,
	        SocialRelationship r4) {

		SocialNode sn15 = getTestSocialNode(15);

		SocialNode sn16 = getTestSocialNode(16);
		SocialNode sn17 = getTestSocialNode(17);
		SocialNode sn18 = getTestSocialNode(18);
		SocialNode sn19 = getTestSocialNode(19);
		
		sn16.addRelationship(sn17, SocialRelationship.ACQUAINTANCE);
		sn16.addRelationship(sn18, SocialRelationship.FAMILY);
		sn16.addRelationship(sn19, SocialRelationship.FAMILY);
		
		sn17.addRelationship(sn16, SocialRelationship.FAMILY);
		sn17.addRelationship(sn18, SocialRelationship.FAMILY);
		sn17.addRelationship(sn19, SocialRelationship.FAMILY);
		
		sn15.addRelationship(sn16, r1);
		sn15.addRelationship(sn17, r2);
		sn15.addRelationship(sn18, r3);
		sn15.addRelationship(sn19, r4);

		return sn15;
	}

	public SocialNode node20With4Relationship(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3,
	        SocialRelationship r4) {

		SocialNode sn20 = getTestSocialNode(20);

		SocialNode sn21 = getTestSocialNode(21);
		SocialNode sn22 = getTestSocialNode(22);
		SocialNode sn23 = getTestSocialNode(23);
		SocialNode sn24 = getTestSocialNode(24);

		sn20.addRelationship(sn21, r1);
		sn20.addRelationship(sn22, r2);
		sn20.addRelationship(sn23, r3);
		sn20.addRelationship(sn24, r4);

		return sn20;
	}
	
	public SocialNode node20With4Relationship25ClusteringCoeff(SocialRelationship r1, SocialRelationship r2, SocialRelationship r3,
	        SocialRelationship r4) {

		SocialNode sn20 = getTestSocialNode(20);

		SocialNode sn21 = getTestSocialNode(21);
		SocialNode sn22 = getTestSocialNode(22);
		SocialNode sn23 = getTestSocialNode(23);
		SocialNode sn24 = getTestSocialNode(24);
		
		sn21.addRelationship(sn22, SocialRelationship.ACQUAINTANCE);
		sn21.addRelationship(sn23, SocialRelationship.ACQUAINTANCE);
		sn21.addRelationship(sn24, SocialRelationship.ACQUAINTANCE);
		
		sn20.addRelationship(sn21, r1);
		sn20.addRelationship(sn22, r2);
		sn20.addRelationship(sn23, r3);
		sn20.addRelationship(sn24, r4);

		return sn20;
	}
}
