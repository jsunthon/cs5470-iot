package models.socialNode;

import models.*;
import models.nodes.Node;
import models.nodes.NodeType;
import models.nodes.SocialNode;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTests {

    @Test()
    /* Test if node returns itself in the result if it already have the feature
    * to discover */
    public void discoverTestSelfPass() {
        SocialNode main = getFeatureNode(0, -999);
        Search searchResult = main.discover(-999);

        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(1, searchResult.getBandwidth());
        Assert.assertEquals(0, searchResult.getNode().getId());
    }

    @Test()
    /* Test if node return unsuccessful search if it has no relationship and does
    * not have the feature. */
    public void discoverTestSelfFail() {
        Node main = getFeatureNode(0, 999);
        Search searchResult = main.discover(1);

        System.out.println(searchResult);

        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(1, searchResult.getBandwidth());
    }


    @Test()
    /* Test if node 2 immediate relationship containsFeature the feature.
     * Should return  true */
    public void discoverTestImmediateRelationship1() {
        SocialNode main = getFeatureNode(0, -1);

        // Relationships
        SocialNode friend1 = getFeatureNode(1, 999, 1);
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        Search searchResult = main.discover(999);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
    }

    @Test()
    /* Should return false if the two immediate relationships does not
    * containsFeature the feature */
    public void discoverTestImmediateRelationship2() {
        SocialNode main = getFeatureNode(0, -1);

        // Relationships
        SocialNode friend1 = getFeatureNode(1, -2, -3);
        SocialNode friend2 = getFeatureNode(2, -4, -5);
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        Search searchResult = main.discover(-999);
        System.out.println(searchResult);

        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(3, searchResult.getBandwidth());
    }


    @Test()
    /* Should check the node with the higher centrality of the node's
     * three immediate relationships FIRST to see if it
     * containsFeature the features */
    public void discoverTestImmediateRelationship3() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addRelationship(getFeatureNode(10, -20), SocialRelationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, -5, -999);
        friend2.addRelationship(getFeatureNode(20, -20), SocialRelationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, -20), SocialRelationship.FAMILY);

        // Friend 3 and his 1 friend
        SocialNode friend3 = getFeatureNode(3, -5, -999);
        friend3.addRelationship(getFeatureNode(30, -20), SocialRelationship.FAMILY);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);
        main.addRelationship(friend3, SocialRelationship.FRIEND);

        Search searchResult = main.discover(-5);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
        Assert.assertEquals(2, searchResult.getNode().getId());
    }

    @Test()
    /* Should check the node with the higher diversity score
     * when the centrality are equal of the node's
     * three immediate relationships FIRST to see if it
     * containsFeature the features */
    public void discoverTestImmediateRelationship4() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addRelationship(getFeatureNode(10, -3), SocialRelationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, -999, -4);
        friend2.addRelationship(getFeatureNode(20, -5), SocialRelationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, -6), SocialRelationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, -999, -7);
        friend3.addRelationship(getFeatureNode(30, -8), SocialRelationship.FAMILY);
        friend3.addRelationship(getFeatureNode(31, -9), SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);
        main.addRelationship(friend3, SocialRelationship.FRIEND);

        Search searchResult = main.discover(-999);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
        Assert.assertEquals(3, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node if immediate relationship does not contain
    *  the relationship but THEIR immediate relationship does. */
    public void discoverDeepRelationshipTest1() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addRelationship(getFeatureNode(10, -16), SocialRelationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, -5, -3);
        friend2.addRelationship(getFeatureNode(20, -9), SocialRelationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, -999), SocialRelationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, -5, -5);
        friend3.addRelationship(getFeatureNode(30,-3), SocialRelationship.FAMILY);
        friend3.addRelationship(getFeatureNode(31, -19), SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);
        main.addRelationship(friend3, SocialRelationship.FRIEND);

        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(8, searchResult.getBandwidth());
        Assert.assertEquals(21, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node if immediate relationship does not contain
    *  the relationship but THEIR immediate relationship does.
    *
    * Test if discover prioritize nodes with higher centrality.
    * Friend2 has lower diversity, but higher centrality, should prioritize it
    */
    public void discoverDeepRelationshipTest2() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addRelationship(getFeatureNode(10, -16), SocialRelationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, -5, -999);
        friend2.addRelationship(getFeatureNode(20, -9), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(21, -18), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(23, -7), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(24, -999), SocialRelationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, -5, -999);
        friend3.addRelationship(getFeatureNode(30,-3), SocialRelationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(31, -19), SocialRelationship.FRIEND);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);
        main.addRelationship(friend3, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-7);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(7, searchResult.getBandwidth());
        Assert.assertEquals(23, searchResult.getNode().getId());
    }

    @Test()
    /* Should fail if immediate relationship does not contain
    *  the relationship AND their immediate relationship does not as well.
    */
    public void discoverDeepRelationshipTest2Fail() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addRelationship(getFeatureNode(10, -16), SocialRelationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, -5, -2);
        friend2.addRelationship(getFeatureNode(20, -9), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(21, -18), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(23, -7), SocialRelationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(24, -2), SocialRelationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, -5, -1);
        friend3.addRelationship(getFeatureNode(30,-3), SocialRelationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(31, -19), SocialRelationship.FRIEND);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);
        main.addRelationship(friend3, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(11, searchResult.getBandwidth());
        Assert.assertNull(searchResult.getNode());
    }

    @Test()
    /* Should return true in a deep nested relationships */
    public void discoverDeepDeepRelationshipTest2Pass() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -6);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, -7), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111,-3), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -6);
        SocialNode f2f2 = getFeatureNode(21, -16);

        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(200, -7), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);
        f2f2.addRelationship(getFeatureNode(210, -8), SocialRelationship.ACQUAINTANCE);
        f2f2.addRelationship(getFeatureNode(211,-3), SocialRelationship.FRIEND);


        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-8);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(10, searchResult.getBandwidth());
        Assert.assertEquals(210, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node in a deep nested relationships
    *  and the feature appear at depth = 1 and 3;
    */
    public void discoverDeepDeepRelationshipTest3Pass() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -6);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100,-3), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111,-3), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -999);  // feature appear at depth 2
        SocialNode f2f2 = getFeatureNode(21, -16);

        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(200, -999), SocialRelationship.ACQUAINTANCE); // feature also appear at depth 3
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);
        f2f2.addRelationship(getFeatureNode(210, -8), SocialRelationship.ACQUAINTANCE);
        f2f2.addRelationship(getFeatureNode(211,-3), SocialRelationship.FRIEND);


        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(4, searchResult.getBandwidth());
        Assert.assertEquals(20, searchResult.getNode().getId());
    }

    @Test()
    /* Test for feature at depth = 4. */
    public void discoverDepth4Test() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -6);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100,-3), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111,-3), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -6);  // feature appear at depth 2
        SocialNode f2f2 = getFeatureNode(21, -16);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -1);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -999); // feature appear here

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());
        System.out.println(searchResult.getFirstNodePaths());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(21101, searchResult.getNode().getId());
    }

    @Test()
    /* Test for feature that occur at depth = 4  and depth = 2.
    * Should return the node that appear in dpeth = 2;
    */
    public void discoverDepth4Test2() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -999);  // feature appear here

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100,-3), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -999), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -6);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -1);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -999);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(7, searchResult.getBandwidth());
        Assert.assertEquals(11, searchResult.getNode().getId());
    }

    @Test()
    /* Test for feature that doesn't appear in network, search up to depth 4 */
    public void discoverDepth4Test2Fail() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -12);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100,-3), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -14), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -6);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -1);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -9);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(null, searchResult.getNode());
    }

    @Test()
    /* Test for feature that occur at depth = 4  and depth = 2.
    * Should return the node that appear in dpeth = 2;
    */
    public void discoverDepth4Test2Limits() {
        SocialNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -1, -2);
        SocialNode f1f1 = getFeatureNode(10, -6);
        SocialNode f1f2 = getFeatureNode(11, -999);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100,-3), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101,-3), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -999), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -1, -2);
        SocialNode f2f1 = getFeatureNode(20, -6);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -2), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -999);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -999);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());
        System.out.println(searchResult.getNodes());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(16, searchResult.getTotalBandwidth());
        Assert.assertEquals(3, searchResult.getNodes().size());
    }
    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */


    public SocialNode getFeatureNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (SocialNode) mf.create(NodeType.SOCIAL, id, Role.BOTH, TimeToLive.HIGH, features);
    }

}
