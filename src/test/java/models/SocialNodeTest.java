package models;

import models.nodes.SocialNode;

import java.util.ArrayList;
import java.util.List;
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


    @Test
    /* Test if sorted edges is sorted by centrality, diversity (tie breaker),
    * If both are tied, whichever relationship get added first is used
    * as the final tiebreaker
    */
    public void sortedEdgesTest1() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);

        // Two relationships
        SocialNode sn1 = node1With2Relationship(
                Relationship.ACQUAINTANCE, Relationship.FAMILY);
        SocialNode sn4 = node4With2Relationship(
                Relationship.ACQUAINTANCE, Relationship.FAMILY);

        // Three relationships
        SocialNode sn7 = node7With3Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY);
        SocialNode sn11 = node11With3Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY);

        // Four relationships
        SocialNode sn15 = node15With4Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY, Relationship.FRIEND
        );
        SocialNode sn20 = node20With4Relationship(
                Relationship.ACQUAINTANCE, Relationship.ACQUAINTANCE,
                Relationship.CO_WORKER, Relationship.FAMILY
        );

        sn0.addRelationship(sn1, Relationship.CO_WORKER);
        sn0.addRelationship(sn4, Relationship.ACQUAINTANCE);
        sn0.addRelationship(sn7, Relationship.FAMILY);
        sn0.addRelationship(sn11, Relationship.FRIEND);
        sn0.addRelationship(sn15, Relationship.FRIEND);
        sn0.addRelationship(sn20, Relationship.FRIEND);

        TreeSet<Edge> edges = (TreeSet<Edge>) sn0.getSortedEdges();
        List<Edge> list = new ArrayList<>(edges);

        Assert.assertEquals(15, list.get(0).getDest().getId());
        Assert.assertEquals(20, list.get(1).getDest().getId());
        Assert.assertEquals(7, list.get(2).getDest().getId());
        Assert.assertEquals(11, list.get(3).getDest().getId());
        Assert.assertEquals(1, list.get(4).getDest().getId());
        Assert.assertEquals(4, list.get(5).getDest().getId());

        System.out.println(edges);
    }

    @Test
    /* Test if sorted edges is sorted by centrality, diversity (tie breaker),
    * If both are tied, whichever relationship get added first is used
    * as the final tiebreaker
    */
    public void sortedEdgesTest2() throws Exception {
        SocialNode sn0 = getTestSocialNode(0);

        // Two relationships
        SocialNode sn1 = node1With2Relationship(
                Relationship.ACQUAINTANCE, Relationship.FAMILY);
        SocialNode sn4 = node4With2Relationship(
                Relationship.ACQUAINTANCE, Relationship.FAMILY);

        // Three relationships
        SocialNode sn7 = node7With3Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY);
        SocialNode sn11 = node11With3Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY);

        // Four relationships
        SocialNode sn15 = node15With4Relationship(
                Relationship.ACQUAINTANCE, Relationship.CO_WORKER,
                Relationship.FAMILY, Relationship.FRIEND
        );
        SocialNode sn20 = node20With4Relationship(
                Relationship.ACQUAINTANCE, Relationship.ACQUAINTANCE,
                Relationship.CO_WORKER, Relationship.FAMILY
        );

        sn0.addRelationship(sn1, Relationship.CO_WORKER);     // 2
        sn0.addRelationship(sn20, Relationship.FRIEND);       // 4

        sn0.addRelationship(sn15, Relationship.FRIEND);       // 4
        sn0.addRelationship(sn11, Relationship.FRIEND);       // 3

        sn0.addRelationship(sn7, Relationship.FAMILY);        // 3
        sn0.addRelationship(sn4, Relationship.ACQUAINTANCE);  // 2

        TreeSet<Edge> edges = (TreeSet<Edge>) sn0.getSortedEdges();
        List<Edge> list = new ArrayList<>(edges);

        Assert.assertEquals(15, list.get(0).getDest().getId());
        Assert.assertEquals(20, list.get(1).getDest().getId());
        Assert.assertEquals(11, list.get(2).getDest().getId());
        Assert.assertEquals(7, list.get(3).getDest().getId());
        Assert.assertEquals(1, list.get(4).getDest().getId());
        Assert.assertEquals(4, list.get(5).getDest().getId());

        System.out.println(edges);
    }
    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */

    public SocialNode getTestSocialNode(int id) {
        return new SocialNode(id, getTestManufacturer(), Role.BOTH, TimeToLive.HIGH);
    }

    public Manufacturer getTestManufacturer() {
        Manufacturer mf = new Manufacturer("test-mf");
        mf.addFeature(Feature.NOISE);
        return mf;
    }

    public SocialNode node1With2Relationship(Relationship r1, Relationship r2) {
        SocialNode sn1 = getTestSocialNode(1);

        SocialNode sn2 = getTestSocialNode(2);
        SocialNode sn3 = getTestSocialNode(3);

        sn1.addRelationship(sn2, r1);
        sn1.addRelationship(sn3, r2);

        return sn1;
    }

    public SocialNode node4With2Relationship(Relationship r1, Relationship r2) {
        SocialNode sn4 = getTestSocialNode(4);

        SocialNode sn5 = getTestSocialNode(5);
        SocialNode sn6 = getTestSocialNode(6);

        sn4.addRelationship(sn5, r1);
        sn4.addRelationship(sn6, r2);

        return sn4;
    }

    public SocialNode node7With3Relationship(Relationship r1, Relationship r2,
                                             Relationship r3) {
        SocialNode sn7 = getTestSocialNode(7);

        SocialNode sn8 = getTestSocialNode(8);
        SocialNode sn9 = getTestSocialNode(9);
        SocialNode sn10 = getTestSocialNode(10);

        sn7.addRelationship(sn8, r1);
        sn7.addRelationship(sn9, r2);
        sn7.addRelationship(sn10, r3);

        return sn7;
    }

    public SocialNode node11With3Relationship(Relationship r1, Relationship r2,
                                              Relationship r3) {
        SocialNode sn11 = getTestSocialNode(11);

        SocialNode sn12 = getTestSocialNode(12);
        SocialNode sn13 = getTestSocialNode(13);
        SocialNode sn14 = getTestSocialNode(14);

        sn11.addRelationship(sn12, r1);
        sn11.addRelationship(sn13, r2);
        sn11.addRelationship(sn14, r3);

        return sn11;
    }

    public SocialNode node15With4Relationship(Relationship r1, Relationship r2,
                                              Relationship r3, Relationship r4) {

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

    public SocialNode node20With4Relationship(Relationship r1, Relationship r2,
                                              Relationship r3, Relationship r4) {

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
}
