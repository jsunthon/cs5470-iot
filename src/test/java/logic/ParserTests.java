package logic;

import java.util.*;

import models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import models.nodes.MasterNode;
import models.nodes.Node;
import models.nodes.NodeType;
import models.nodes.SlaveNode;
import models.nodes.SocialNode;
import models.nodes.DecentralizedNode;

public class ParserTests {

	@Test
	public void testGetOrCreateNode() {
		Parser parser = initParser();
		Node[] nodes = new Node[5];
		Node node1 = parser.getOrCreateNode(nodes, NodeType.SOCIAL, 3, new Integer[]{4, 5, 6});
		Assert.assertEquals(3, node1.getId());
		Assert.assertTrue(node1.getFeatures().contains(6));
		Node node2 = parser.getOrCreateNode(nodes, NodeType.DECENTRAL, 3, new Integer[]{4, 1, 3});
		Assert.assertEquals(node1, node2);
		Node node3 = parser.getOrCreateNode(nodes, NodeType.MASTER, 4, new Integer[]{1, 5, 10});
		Assert.assertEquals(4, node3.getId());
		Assert.assertTrue(node3.getFeatures().contains(10));
		Assert.assertTrue(node3.getFeatures().contains(1));
		Assert.assertTrue(node3.getFeatures().contains(5));
		Assert.assertFalse(node3.getFeatures().contains(3));
	}

	@Test
	public void testGenSocialNode() {
		Parser parser = initParser();
		parser.initNodeArrays(10000);
		JSONObject relation1 = genRelationObj(9137, genFeatures(3, 5, 7), 2);
		JSONObject relation2 = genRelationObj(934, genFeatures(100, 1, 200), 1);
		JSONObject relation3 = genRelationObj(7126, genFeatures(1001, 60, 257), 4);
		JSONArray relations = new JSONArray();
		relations.add(relation1);
		relations.add(relation2);
		relations.add(relation3);
		JSONArray jsonArray1 = new JSONArray();
		SocialNode node1 = parser.genSocialNode(1, genFeatures(5, 101, 503), relations.iterator());
		SocialNode node2 = parser.genSocialNode(10000, genFeatures(9, 11, 1002), relations.iterator());
		Assert.assertEquals(node1, parser.getSocialNodes()[1]);
		Assert.assertEquals(node2, parser.getSocialNodes()[10000]);
		Assert.assertNull(parser.getSocialNodes()[0]);
		Assert.assertEquals(1, node1.getId());
		Assert.assertNull(parser.getSocialNodes()[1001]);
		Assert.assertTrue(node1.getFeatures().contains(5));
		Assert.assertTrue(node1.getFeatures().contains(101));
		Assert.assertTrue(node1.getFeatures().contains(503));
		Assert.assertFalse(node1.getFeatures().contains(504));
		Assert.assertEquals(3, node1.getRelationshipMap().entrySet().size());
		testRelations(node1, relations.iterator());
		testRelations(node2, relations.iterator());
	}

	@Test
	public void testParseAndGenSocial() {
		Parser parser = initParser();
		parser.parseAndGenSocial("./src/main/javascript/node-20k-feat-1k.json");
		Node[] nodes = parser.getSocialNodes();
		Assert.assertEquals(20001, nodes.length);
		Assert.assertNotNull(nodes[20000]);
		Assert.assertNotNull(nodes[1]);
		Assert.assertNull(nodes[0]);
		Assert.assertTrue(((SocialNode) nodes[1]).getFeatures().contains(325));
		Assert.assertTrue(((SocialNode) nodes[1]).getFeatures().contains(409));
		Assert.assertTrue(((SocialNode) nodes[20000]).getFeatures().contains(163));
		Assert.assertTrue(((SocialNode) nodes[20000]).getFeatures().contains(413));
	}

	@Test
	public void testGenNodeFromSocial() {
		Parser parser = initParser();
		SocialNode socialNode1 = genSocialNode(1, new Integer[]{5, 1, 23}, "J", "M");
		MasterNode masterNode = (MasterNode) parser.genNodeFromSocial(socialNode1, NodeType.MASTER);
		testSocialCopy(socialNode1, masterNode);
		SocialNode socialNode2 = genSocialNode(100, new Integer[]{50, 71, 1023}, "S", "L");
		Assert.assertNotNull(socialNode2);
		SlaveNode slaveNode = (SlaveNode) parser.genNodeFromSocial(socialNode2, NodeType.SLAVE);
		testSocialCopy(socialNode2, slaveNode);
		masterNode.addSlaveNode(slaveNode);
		Assert.assertTrue(masterNode.hasSlaveNode(slaveNode));
	}

	@Test
	public void testGenCentral() {
		Parser parser = initParser();
		parser.parseAndGenSocial("./src/main/javascript/node-20k-feat-1k.json");
		Assert.assertTrue(parser.genCentral());
		Node[] socialNodes = parser.getSocialNodes();
		Node[] centralNodes = parser.getCentralNodes();
		Assert.assertNotNull(socialNodes);
		Assert.assertNotNull(centralNodes);
		Assert.assertEquals(socialNodes.length, centralNodes.length);
		MasterNode masterNode = null;
		for (int i = 1; i < socialNodes.length; i++) {
			SocialNode socialNode = (SocialNode) socialNodes[i];
			Assert.assertNotNull(socialNode);
			if (i == 1) {
				masterNode = (MasterNode) centralNodes[i];
				Assert.assertNotNull(masterNode);
				testSocialCopy(socialNode, masterNode);
			} else {
				Assert.assertNotNull(masterNode);
				SlaveNode slaveNode = (SlaveNode) centralNodes[i];
				Assert.assertNotNull(slaveNode);
				masterNode.addSlaveNode(slaveNode);
				testSocialCopy(socialNode, slaveNode);
				Assert.assertTrue(masterNode.hasSlaveNode(slaveNode));
			}
		}
	}

	@Test
	public void testGetOrCreateDecenNode() {
		Parser parser = initParser();
		parser.initNodeArrays(10000);
		JSONObject relation1 = genRelationObj(9137, genFeatures(3, 5, 7), 2);
		JSONObject relation2 = genRelationObj(934, genFeatures(100, 1, 200), 1);
		JSONObject relation3 = genRelationObj(7126, genFeatures(1001, 60, 257), 4);
		JSONArray relations = new JSONArray();
		relations.add(relation1);
		relations.add(relation2);
		relations.add(relation3);
		SocialNode node1 = parser.genSocialNode(1, genFeatures(5, 101, 503), relations.iterator());
		SocialNode node2 = parser.genSocialNode(2, genFeatures(5, 101, 503), relations.iterator());
		SocialNode node10000 = parser.genSocialNode(10000, genFeatures(5, 101, 503), relations.iterator());
		DecentralizedNode decenNode1 = (DecentralizedNode) parser.getOrCreateDecenNode(node1);
		DecentralizedNode decenNode2 = (DecentralizedNode) parser.getOrCreateDecenNode(node2);
		DecentralizedNode decenNode10000 = (DecentralizedNode) parser.getOrCreateDecenNode(node10000);
		Assert.assertNotNull(decenNode1);
		Assert.assertNotNull(decenNode2);
		Assert.assertNotNull(decenNode10000);
		Node[] decentralized = parser.getDecentralNodes();
		Assert.assertNotNull(decentralized[1]);
		Assert.assertNotNull(decentralized[2]);
		Assert.assertNotNull(decentralized[10000]);
		testSocialCopy(node1, decenNode1);
		testSocialCopy(node2, decenNode2);
		testSocialCopy(node10000, decenNode10000);
	}

	@Test
	public void testGenDecentral() {
		Parser parser = initParser();
		parser.parseAndGenSocial("./src/main/javascript/node-20k-feat-1k.json");
		Assert.assertTrue(parser.genDecentral());
		Node[] socialNodes = parser.getSocialNodes();
		Node[] decentralNodes = parser.getDecentralNodes();
		Assert.assertEquals(socialNodes.length, decentralNodes.length);
		for (int i = 1; i < socialNodes.length; i++) {
			SocialNode socialNode = (SocialNode) socialNodes[i];
			DecentralizedNode decenNode = (DecentralizedNode) decentralNodes[i];
			Assert.assertNotNull(socialNode);
			Assert.assertNotNull(decenNode);
			testDecentralSocialCopy(socialNode, decenNode);
		}
	}

	public void testRelations(SocialNode node, Iterator<JSONObject> relations) {
		while (relations.hasNext()) {
			JSONObject relation = relations.next();
			int id = ((Long) relation.get("id")).intValue();
			JSONArray features = (JSONArray) relation.get("features");
			int type = ((Long) relation.get("type")).intValue();
			Assert.assertTrue(node.edgeExists(id, featArrToSet(features), type));
		}
	}

	private static void testSocialCopy(SocialNode socialNode, Node node) {
		Assert.assertEquals(socialNode.getId(), node.getId());
		Assert.assertEquals(socialNode.getFeatures(), node.getFeatures());
		Assert.assertEquals(socialNode.getOwner(), node.getOwner());
		Assert.assertEquals(socialNode.getManufacturer(), node.getManufacturer());
		Assert.assertEquals(socialNode.getShare(), node.getShare());
		Assert.assertEquals(socialNode.getOwner(), node.getOwner());
		Assert.assertEquals(socialNode.getTimeToLive(), node.getTimeToLive());
		Assert.assertEquals(socialNode.getRole(), node.getRole());
	}

	private static void testDecentralSocialCopy(SocialNode socialNode, DecentralizedNode node) {
		Set<Neighbor> decenNeighbors = node.getNeighbors();
		Set<Relationship> socialEdges = socialNode.getSortedRelationships();
		Assert.assertEquals(decenNeighbors.size(), socialEdges.size());
		for (Neighbor neighbor : decenNeighbors) {
			Assert.assertTrue(socialEdgesContainsNeighbor(neighbor, socialEdges));
		}
		testSocialCopy(socialNode, node);
	}

	private static boolean socialEdgesContainsNeighbor(Neighbor neighbor, Set<Relationship> socialEdges) {
		if (neighbor != null) {
			for (Relationship socialEdge : socialEdges) {
				SocialNode socialNode = socialEdge.getDest();
				if (socialNode.getId() == neighbor.getDest().getId()) return true;
			}
		}
		return false;
	}

	private static SocialNode genSocialNode(
			int id,
			Integer[] features,
			String ownerName,
			String manufName) {
		RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
		Owner owner = new Owner(ownerName);
		Manufacturer manuf = new Manufacturer(manufName);
		SocialNode socialNode = (SocialNode) randEnvGen.genNode(NodeType.SOCIAL,
				id, owner, manuf, Role.randomRole(), TimeToLive.randomTimeToLive(),
				features, true);
		return socialNode;
	}

	private static Parser initParser() {
		RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
		randEnvGen.reset();
		randEnvGen.genManufacturers(10, 4);
		randEnvGen.genOwners(2);
		return new Parser();
	}

	private static JSONObject genRelationObj(int id, JSONArray features, int type) {
		JSONObject relationObj = new JSONObject();
		relationObj.put("id", new Long(id));
		relationObj.put("features", features);
		relationObj.put("type", new Long(type));
		return relationObj;
	}


	private static JSONArray genFeatures(int i, int j, int k) {
		JSONArray features = new JSONArray();
		features.add(new Long(i));
		features.add(new Long(j));
		features.add(new Long(k));
		return features;
	}

	private static Set<Integer> featArrToSet(JSONArray featArr) {
		Set<Integer> features = new HashSet<>();
		for (int i = 0; i < featArr.size(); i++) {
			features.add(((Long) featArr.get(i)).intValue());
		}
		return features;
	}

	// @Test
	// public void testCreateOrGetSocialNode() {
	// RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
	// randEnvGen.reset();
	// randEnvGen.genManufacturers(10, 4);
	// randEnvGen.genOwners(2);
	// Parser parser = new Parser();
	// SocialNode node1 = parser.createOrGetSocialNode(1);
	// Assert.assertNotNull(node1);
	// SocialNode node2 = parser.createOrGetSocialNode(2);
	// Assert.assertNotNull(node2);
	// }

	// @Test
	// public void testFormRandomRelationship() {
	// RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
	// randEnvGen.reset();
	// randEnvGen.genManufacturers(10, 4);
	// randEnvGen.genOwners(2);
	// Parser parser = new Parser();
	// SocialNode node1 = parser.createOrGetSocialNode(1);
	// List<Long> neighbors = new ArrayList<>();
	// neighbors.add(new Long(2));
	// neighbors.add(new Long(4));
	// neighbors.add(new Long(6));
	// parser.formRandomRelationship(node1, neighbors.iterator());
	// Assert.assertNotNull(node1);
	// Assert.assertEquals(3, (int) node1.getCentrality());
	// neighbors.add(new Long(5));
	// Assert.assertEquals(4, neighbors.size());
	// parser.formRandomRelationship(node1, neighbors.iterator());
	// Assert.assertEquals(4, (int) node1.getCentrality());
	// SocialNode node2 = parser.createOrGetSocialNode(4);
	// List<Long> neighbors2 = new ArrayList<>();
	// neighbors2.add(new Long(5));
	// neighbors2.add(new Long(10));
	// parser.formRandomRelationship(node2, neighbors2.iterator());
	// Assert.assertEquals(2, (int) node2.getCentrality());
	// }
	//
	// @Test
	// public void testFormRelationship() {
	// RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
	// randEnvGen.reset();
	// randEnvGen.genManufacturers(10, 4);
	// randEnvGen.genOwners(2);
	// Parser parser = new Parser();
	// //simulate social-network.json
	// List<JSONObject> node0Neighbors = new ArrayList<>();
	// JSONObject neighbor_0_1 = new JSONObject();
	// neighbor_0_1.put("id", 1);
	// neighbor_0_1.put("relationship", "CO_WORKER");
	// JSONObject neighbor_0_3 = new JSONObject();
	// neighbor_0_3.put("id", 3);
	// neighbor_0_3.put("relationship", "FAMILY");
	// JSONObject neighbor_0_4 = new JSONObject();
	// neighbor_0_4.put("id", 4);
	// neighbor_0_4.put("relationship", "FAMILY");
	// node0Neighbors.add(neighbor_0_1);
	// node0Neighbors.add(neighbor_0_3);
	// node0Neighbors.add(neighbor_0_4);
	// SocialNode node0 = parser.createOrGetSocialNode(0);
	// Assert.assertNotNull(node0);
	// parser.formRelationship(node0, node0Neighbors.iterator());
	// Map<SocialRelationship, LinkedList<Relationship>> map = node0.getRelationshipMap();
	// Assert.assertNotNull(map.get(SocialRelationship.CO_WORKER));
	// Assert.assertNotNull(map.get(SocialRelationship.FAMILY));
	// LinkedList<Relationship> coWorkers = map.get(SocialRelationship.CO_WORKER);
	// for (Relationship edge : coWorkers) {
	// SocialNode src = edge.getSrc();
	// SocialNode dst = edge.getDest();
	// SocialRelationship relationship = edge.getSocialRelationship();
	// Assert.assertEquals((Integer) 0, (Integer) src.getId());
	// Assert.assertEquals((Integer) 1, (Integer) dst.getId());
	// Assert.assertEquals(SocialRelationship.CO_WORKER
	// , relationship);
	// break;
	// }
	// LinkedList<Relationship> family = map.get(SocialRelationship.FAMILY);
	// Relationship edgeTarget = null;
	// for (Relationship edge : family) {
	// if (edge.getSrc().getId() == 0
	// && edge.getDest().getId() == 3
	// && edge.getSocialRelationship().equals(SocialRelationship.FAMILY)) {
	// edgeTarget = edge;
	// break;
	// }
	// }
	// Assert.assertNotNull(edgeTarget);
	// edgeTarget = null;
	// Assert.assertNull(edgeTarget);
	// for (Relationship edge : family) {
	// if (edge.getSrc().getId() == 0
	// && edge.getDest().getId() == 4
	// && edge.getSocialRelationship().equals(SocialRelationship.FAMILY)) {
	// edgeTarget = edge;
	// break;
	// }
	// }
	// Assert.assertNotNull(edgeTarget);
	// }
}
