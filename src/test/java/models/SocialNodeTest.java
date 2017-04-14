package models;

import models.nodes.SocialNode;

import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

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
        sn0.addRelationship(sn1, Relationship.FRIEND);

        Assert.assertEquals(1, sn0.getCentrality());
    }

    @Test
    // Test Centrality when the Node has two relation (that has 0 relationship each)
    public void centralityTest3() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);
        SocialNode sn1 = getTestSocialNode(1);
        SocialNode sn2 = getTestSocialNode(2);
        sn0.addRelationship(sn1, Relationship.FRIEND);
        sn0.addRelationship(sn2, Relationship.FRIEND);

        Assert.assertEquals(2, sn0.getCentrality());
    }

    @Test
    // Test Centrality when the Node has two relation (that has 1 relationship each)
    public void centralityTest4() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);

        SocialNode sn1 = getTestSocialNode(1);
        SocialNode sn2 = getTestSocialNode(2);
        sn1.addRelationship(sn2, Relationship.FAMILY);

        SocialNode sn3 = getTestSocialNode(3);
        SocialNode sn4 = getTestSocialNode(4);
        sn3.addRelationship(sn4, Relationship.FRIEND);

        sn0.addRelationship(sn1, Relationship.FRIEND);
        sn0.addRelationship(sn4, Relationship.FAMILY);

        Assert.assertEquals(2, sn0.getCentrality());
    }


    @Test
    // Test Centrality when the Node has two relation (that has 1 relationship each)
    public void centralityTest5() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);

        SocialNode sn1 = getTestSocialNode(1);
        SocialNode sn2 = getTestSocialNode(2);
        sn1.addRelationship(sn2, Relationship.FRIEND);

        SocialNode sn3 = getTestSocialNode(3);
        SocialNode sn4 = getTestSocialNode(4);
        sn3.addRelationship(sn4, Relationship.FRIEND);

        sn0.addRelationship(sn1, Relationship.FRIEND);
        sn0.addRelationship(sn4, Relationship.FAMILY);

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
        sn1.addRelationship(sn2, Relationship.FRIEND);
        sn1.addRelationship(sn3, Relationship.FAMILY);

        SocialNode sn4 = getTestSocialNode(4);
        SocialNode sn5 = getTestSocialNode(5);
        SocialNode sn6 = getTestSocialNode(5);
        sn4.addRelationship(sn5, Relationship.FRIEND);
        sn4.addRelationship(sn6, Relationship.FRIEND);

        sn0.addRelationship(sn1, Relationship.FRIEND);
        sn0.addRelationship(sn4, Relationship.FRIEND);

        Assert.assertEquals("should be 2", 2, sn0.getCentrality());
    }


//    @Test
//    // The edgeList should be sorted by the edge with the highest centrality first,
//    // all else being equal.
//    public void edgeListCentrality1() throws Exception {
//        SocialNode sn0 = getTestSocialNode(0);
//
//        SocialNode sn1 = node1With3Relationship();
//        SocialNode sn5 = node5With2Relationship();
//        SocialNode sn8 = node8With4Relationship();
//
//        sn0.addRelationship(sn1, Relationship.FRIEND);
//        sn0.addRelationship(sn5, Relationship.CO_WORKER);
//        sn0.addRelationship(sn8, Relationship.ACQUAINTANCE);
//
//        System.out.println(sn0.getEdgeList());
//        Edge edge1 = sn0.getEdgeList().get(0);
//        Edge edge2 = sn0.getEdgeList().get(1);
//        Edge edge3 = sn0.getEdgeList().get(2);
//
//        Assert.assertEquals(8, edge1.getDest().getId());
//        Assert.assertEquals(5, edge2.getDest().getId());
//        Assert.assertEquals(1, edge3.getDest().getId());
//    }
//
//    @Test
//    // The edgeList should be sorted by the edge with the highest centrality first,
//    // all else being equal.
//    public void edgeListCentrality2() throws Exception {
//        SocialNode sn0 = getTestSocialNode(0);
//
//
//        SocialNode sn5 = node5With2Relationship();
//        SocialNode sn1 = node1With3Relationship();
//        SocialNode sn8 = node8With4Relationship();
//
//        sn0.addRelationship(sn5, Relationship.CO_WORKER);
//        sn0.addRelationship(sn1, Relationship.CO_WORKER);
//        sn0.addRelationship(sn8, Relationship.FRIEND);
//
//
//        System.out.println(sn0.getEdgeList());
//        Edge edge1 = sn0.getEdgeList().get(0);
//        Edge edge2 = sn0.getEdgeList().get(1);
//        Edge edge3 = sn0.getEdgeList().get(2);
//
//        Assert.assertEquals(1, edge1.getDest().getId());
//        Assert.assertEquals(5, edge2.getDest().getId());
//        Assert.assertEquals(8, edge3.getDest().getId());
//    }
//
//    @Test
//    // The edgeList should be sorted by the edge with the highest centrality first,
//    // all else being equal.
//    public void edgeListCentrality3() throws Exception {
//        SocialNode sn0 = getTestSocialNode(0);
//
//        SocialNode sn5 = node5With2Relationship();
//        SocialNode sn1 = node1With3Relationship();
//        SocialNode sn8 = node8With4Relationship();
//        SocialNode sn12 = node12With3Relationship();
//
//        sn0.addRelationship(sn5, Relationship.CO_WORKER);
//        sn0.addRelationship(sn1, Relationship.FAMILY);
//        sn0.addRelationship(sn8, Relationship.ACQUAINTANCE);
//        sn0.addRelationship(sn12, Relationship.FAMILY);
//
//        System.out.println(sn0.getEdgeList());
//        Edge edge1 = sn0.getEdgeList().get(0);
//        Edge edge2 = sn0.getEdgeList().get(1);
//        Edge edge3 = sn0.getEdgeList().get(2);
//        Edge edge4 = sn0.getEdgeList().get(3);
//
//
//        Assert.assertEquals(1, edge1.getDest().getId());
//        Assert.assertEquals(12, edge2.getDest().getId());
//        Assert.assertEquals(8, edge3.getDest().getId());
//        Assert.assertEquals(5, edge4.getDest().getId());
//
//    }
//
//    @Test
//    // The edgeList should be sorted by the edge with the highest centrality first,
//    // all else being equal.
//    public void edgeListCentrality4() throws Exception {
//        SocialNode sn0 = getTestSocialNode(0);
//
//        SocialNode sn5 = node5With2Relationship();
//        SocialNode sn1 = node1With3Relationship();
//        SocialNode sn8 = node8With4Relationship();
//        SocialNode sn12 = node12With3Relationship();
//        SocialNode sn16 = node16With4Relationship();
//
//        sn0.addRelationship(sn5, Relationship.ACQUAINTANCE);
//        sn0.addRelationship(sn1, Relationship.CO_WORKER);
//        sn0.addRelationship(sn8, Relationship.FRIEND);
//        sn0.addRelationship(sn12, Relationship.CO_WORKER);
//        sn0.addRelationship(sn16, Relationship.FAMILY);
//
//        System.out.println(sn0.getEdgeList());
//        Edge edge1 = sn0.getEdgeList().get(0);
//        Edge edge2 = sn0.getEdgeList().get(1);
//        Edge edge3 = sn0.getEdgeList().get(2);
//        Edge edge4 = sn0.getEdgeList().get(3);
//        Edge edge5 = sn0.getEdgeList().get(4);
//
//
//        Assert.assertEquals(1, edge1.getDest().getId());
//        Assert.assertEquals(12, edge2.getDest().getId());
//        Assert.assertEquals(16, edge3.getDest().getId());
//        Assert.assertEquals(8, edge4.getDest().getId());
//        Assert.assertEquals(5, edge5.getDest().getId());
//
//    }
    
    @Test
    // The edgeList should be sorted by the edge with the highest centrality first,
    // all else being equal.
    public void edgeListCentrality5() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);

        SocialNode sn5 = node5With2Relationship();
        SocialNode sn1 = node1With3Relationship();
        SocialNode sn8 = node8With4Relationship();
        SocialNode sn12 = node12With3Relationship();
        SocialNode sn16 = node16With4Relationship();
        SocialNode sn21 = node21With4Relationship();
        
        sn0.addRelationship(sn5, Relationship.ACQUAINTANCE);
        sn0.addRelationship(sn12, Relationship.CO_WORKER);
        sn0.addRelationship(sn1, Relationship.CO_WORKER);
        sn0.addRelationship(sn8, Relationship.FRIEND);
    
        sn0.addRelationship(sn21, Relationship.ACQUAINTANCE);
        sn0.addRelationship(sn16, Relationship.FRIEND);
        
      
//        System.out.println(sn0.getEdgeList());
        TreeSet<Edge> edges = (TreeSet<Edge>) sn0.getSortedEdges();
        System.out.println(edges);
//        for (Edge edge : edges) {
//        	System.out.println(edge.toString());
//        }
//        Edge edge2 = sn0.getEdgeList().get(1);
//        Edge edge3 = sn0.getEdgeList().get(2);
//        Edge edge4 = sn0.getEdgeList().get(3);
//        Edge edge5 = sn0.getEdgeList().get(4);
//        Edge edge6 = sn0.getEdgeList().get(5);

//
//        Assert.assertEquals(1, edge1.getDest().getId());
//        Assert.assertEquals(12, edge2.getDest().getId());
//        Assert.assertEquals(16, edge3.getDest().getId());
//        Assert.assertEquals(8, edge4.getDest().getId());
//        Assert.assertEquals(5, edge5.getDest().getId());
//        

    }

    public SocialNode getTestSocialNode(int id) {
        return new SocialNode(id, getTestManufacturer(), Role.BOTH, TimeToLive.HIGH);

    }

    public Manufacturer getTestManufacturer() {
        Manufacturer mf = new Manufacturer("test-mf");
        mf.addFeature(Feature.NOISE);
        return mf;
    }

    public SocialNode node5With2Relationship() {
        SocialNode sn5 = getTestSocialNode(5);
        SocialNode sn6 = getTestSocialNode(6);
        SocialNode sn7 = getTestSocialNode(7);

        sn5.addRelationship(sn6, Relationship.FAMILY);
        sn5.addRelationship(sn7, Relationship.CO_WORKER);
        return sn5;
    }

    public SocialNode node1With3Relationship() {
        SocialNode sn1 = getTestSocialNode(1);
        SocialNode sn2 = getTestSocialNode(2);
        SocialNode sn3 = getTestSocialNode(3);
        SocialNode sn4 = getTestSocialNode(4);

        sn1.addRelationship(sn2, Relationship.FAMILY);
        sn1.addRelationship(sn3, Relationship.CO_WORKER);
        sn1.addRelationship(sn4, Relationship.CO_WORKER);
        return sn1;
    }

    public SocialNode node12With3Relationship() {
        SocialNode sn12 = getTestSocialNode(12);

        SocialNode sn13 = getTestSocialNode(13);
        SocialNode sn14 = getTestSocialNode(14);
        SocialNode sn15 = getTestSocialNode(15);

        sn12.addRelationship(sn13, Relationship.FAMILY);
        sn12.addRelationship(sn14, Relationship.CO_WORKER);
        sn12.addRelationship(sn15, Relationship.CO_WORKER);
        return sn12;
    }

    public SocialNode node8With4Relationship() {
        SocialNode sn8 = getTestSocialNode(8);
        SocialNode sn9 = getTestSocialNode(9);
        SocialNode sn10 = getTestSocialNode(10);
        SocialNode sn11 = getTestSocialNode(11);
        SocialNode sn12 = getTestSocialNode(11);

        sn8.addRelationship(sn9, Relationship.FAMILY);
        sn8.addRelationship(sn10, Relationship.FAMILY);
        sn8.addRelationship(sn11, Relationship.FAMILY);
        sn8.addRelationship(sn12, Relationship.FAMILY);
        return sn8;
    }

    public SocialNode node16With4Relationship() {
        SocialNode sn16 = getTestSocialNode(16);
        SocialNode sn17 = getTestSocialNode(17);
        SocialNode sn18 = getTestSocialNode(18);
        SocialNode sn19 = getTestSocialNode(19);
        SocialNode sn20 = getTestSocialNode(20);

        sn16.addRelationship(sn17, Relationship.FAMILY);
        sn16.addRelationship(sn18, Relationship.FAMILY);
        sn16.addRelationship(sn19, Relationship.CO_WORKER);
        sn16.addRelationship(sn20, Relationship.CO_WORKER);
        return sn16;
    }
    
    public SocialNode node21With4Relationship() {
        SocialNode sn21 = getTestSocialNode(21);
        SocialNode sn22 = getTestSocialNode(22);
        SocialNode sn23 = getTestSocialNode(23);
        SocialNode sn24 = getTestSocialNode(24);
        SocialNode sn25 = getTestSocialNode(25);

        sn21.addRelationship(sn22, Relationship.FAMILY);
        sn21.addRelationship(sn23, Relationship.FAMILY);
        sn21.addRelationship(sn24, Relationship.FRIEND);
        sn21.addRelationship(sn25, Relationship.FRIEND);
        
        return sn21;
    }
}
