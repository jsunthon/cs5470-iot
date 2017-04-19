package logic;

import java.util.*;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.App;
import models.Edge;
import models.Relationship;
import models.nodes.SocialNode;

public class ParserTests {

    @Test
    public void testCreateOrGetSocialNode() {
        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.reset();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        SocialNode node1 = parser.createOrGetSocialNode(1);
        Assert.assertNotNull(node1);
        SocialNode node2 = parser.createOrGetSocialNode(2);
        Assert.assertNotNull(node2);
    }

    @Test
    public void testFormRandomRelationship() {
        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.reset();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        SocialNode node1 = parser.createOrGetSocialNode(1);
        List<Long> neighbors = new ArrayList<>();
        neighbors.add(new Long(2));
        neighbors.add(new Long(4));
        neighbors.add(new Long(6));
        parser.formRandomRelationship(node1, neighbors.iterator());
        Assert.assertNotNull(node1);
        Assert.assertEquals(3, (int) node1.getCentrality());
        neighbors.add(new Long(5));
        Assert.assertEquals(4, neighbors.size());
        parser.formRandomRelationship(node1, neighbors.iterator());
        Assert.assertEquals(4, (int) node1.getCentrality());
        SocialNode node2 = parser.createOrGetSocialNode(4);
        List<Long> neighbors2 = new ArrayList<>();
        neighbors2.add(new Long(5));
        neighbors2.add(new Long(10));
        parser.formRandomRelationship(node2, neighbors2.iterator());
        Assert.assertEquals(2, (int) node2.getCentrality());
    }

    @Test
    public void testFormRelationship() {
        RandEnvGenerator randEnvGen = RandEnvGenerator.getInstance();
        randEnvGen.reset();
        randEnvGen.genManufacturers(10, 4);
        randEnvGen.genOwners(2);
        Parser parser = new Parser();
        //simulate social-network.json
        List<JSONObject> node0Neighbors = new ArrayList<>();
        JSONObject neighbor_0_1 = new JSONObject();
        neighbor_0_1.put("id", 1);
        neighbor_0_1.put("relationship", "CO_WORKER");
        JSONObject neighbor_0_3 = new JSONObject();
        neighbor_0_3.put("id", 3);
        neighbor_0_3.put("relationship", "FAMILY");
        JSONObject neighbor_0_4 = new JSONObject();
        neighbor_0_4.put("id", 4);
        neighbor_0_4.put("relationship", "FAMILY");
        node0Neighbors.add(neighbor_0_1);
        node0Neighbors.add(neighbor_0_3);
        node0Neighbors.add(neighbor_0_4);
        SocialNode node0 = parser.createOrGetSocialNode(0);
        Assert.assertNotNull(node0);
        parser.formRelationship(node0, node0Neighbors.iterator());
        Map<Relationship, LinkedList<Edge>> map = node0.getRelationshipMap();
        Assert.assertNotNull(map.get(Relationship.CO_WORKER));
        Assert.assertNotNull(map.get(Relationship.FAMILY));
        LinkedList<Edge> coWorkers = map.get(Relationship.CO_WORKER);
        for (Edge edge : coWorkers) {
            SocialNode src = edge.getSrc();
            SocialNode dst = edge.getDest();
            Relationship relationship = edge.getRelationship();
            Assert.assertEquals((Integer) 0, (Integer) src.getId());
            Assert.assertEquals((Integer) 1, (Integer) dst.getId());
            Assert.assertEquals(Relationship.CO_WORKER
                    , relationship);
            break;
        }
        LinkedList<Edge> family = map.get(Relationship.FAMILY);
        Edge edgeTarget = null;
        for (Edge edge : family) {
            if (edge.getSrc().getId() == 0
                    && edge.getDest().getId() == 3
                    && edge.getRelationship().equals(Relationship.FAMILY)) {
                edgeTarget = edge;
                break;
            }
        }
        Assert.assertNotNull(edgeTarget);
        edgeTarget = null;
        Assert.assertNull(edgeTarget);
        for (Edge edge : family) {
            if (edge.getSrc().getId() == 0
                    && edge.getDest().getId() == 4
                    && edge.getRelationship().equals(Relationship.FAMILY)) {
                edgeTarget = edge;
                break;
            }
        }
        Assert.assertNotNull(edgeTarget);
    }
}
