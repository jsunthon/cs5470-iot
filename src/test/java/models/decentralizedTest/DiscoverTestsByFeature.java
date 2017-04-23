package models.decentralizedTest;

import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;
import models.nodes.DecentralizedNode;
import models.nodes.Node;
import models.nodes.NodeType;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTestsByFeature {

    @Test()
    /* Test if node returns itself in the result if it already have the feature
    * to discover */
    public void discoverTestSelfPass() {
        DecentralizedNode main = getFeatureNode(0, -999);
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
        DecentralizedNode main = getFeatureNode(0, -1);

        // Relationships
        DecentralizedNode friend1 = getFeatureNode(1, 999, 1);
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        Search searchResult = main.discover(999);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
    }

    @Test()
    /* Should return false if the two immediate relationships does not
    * containsFeature the feature */
    public void discoverTestImmediateRelationship2() {
        DecentralizedNode main = getFeatureNode(0, -1);

        // Relationships
        DecentralizedNode friend1 = getFeatureNode(1, -2, -3);
        DecentralizedNode friend2 = getFeatureNode(2, -4, -5);
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

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
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addNeighbor(getFeatureNode(10, -20));

        // Friend 2 and his 2 friends
        DecentralizedNode friend2 = getFeatureNode(2, -5, -999);
        friend2.addNeighbor(getFeatureNode(20, -20));
        friend2.addNeighbor(getFeatureNode(21, -20));

        // Friend 3 and his 1 friend
        DecentralizedNode friend3 = getFeatureNode(3, -5, -999);
        friend3.addNeighbor(getFeatureNode(30, -20));

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);
        main.addNeighbor(friend3);

        Search searchResult = main.discover(-5);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(3, searchResult.getBandwidth());
        Assert.assertEquals(2, searchResult.getNode().getId());
    }

    @Test()
    /* Should check the node with the higher diversity score
     * when the centrality are equal of the node's
     * three immediate relationships FIRST to see if it
     * containsFeature the features */
    public void discoverTestImmediateRelationship4() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addNeighbor(getFeatureNode(10, -3));

        // Friend 2 and his 2 friends
        DecentralizedNode friend2 = getFeatureNode(2, -999, -4);
        friend2.addNeighbor(getFeatureNode(20, -5));
        friend2.addNeighbor(getFeatureNode(21, -6));

        // Friend 3 and his 2 friend
        DecentralizedNode friend3 = getFeatureNode(3, -999, -7);
        friend3.addNeighbor(getFeatureNode(30, -8));
        friend3.addNeighbor(getFeatureNode(31, -9));

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);
        main.addNeighbor(friend3);

        Search searchResult = main.discover(-999);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(3, searchResult.getBandwidth());
        Assert.assertEquals(2, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node if immediate relationship does not contain
    *  the relationship but THEIR immediate relationship does. */
    public void discoverDeepRelationshipTest1() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addNeighbor(getFeatureNode(10, -16));

        // Friend 2 and his 2 friends
        DecentralizedNode friend2 = getFeatureNode(2, -5, -3);
        friend2.addNeighbor(getFeatureNode(20, -9));
        friend2.addNeighbor(getFeatureNode(21, -999));

        // Friend 3 and his 2 friend
        DecentralizedNode friend3 = getFeatureNode(3, -5, -5);
        friend3.addNeighbor(getFeatureNode(30,-3));
        friend3.addNeighbor(getFeatureNode(31, -19));

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);
        main.addNeighbor(friend3);

        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(7, searchResult.getBandwidth());
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
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addNeighbor(getFeatureNode(10, -16));

        // Friend 2 and his 4 friends
        DecentralizedNode friend2 = getFeatureNode(2, -5, -999);
        friend2.addNeighbor(getFeatureNode(20, -9));
        friend2.addNeighbor(getFeatureNode(21, -18));
        friend2.addNeighbor(getFeatureNode(23, -7));
        friend2.addNeighbor(getFeatureNode(24, -999));

        // Friend 3 and his 2 friend
        DecentralizedNode friend3 = getFeatureNode(3, -5, -999);
        friend3.addNeighbor(getFeatureNode(30,-3));
        friend3.addNeighbor(getFeatureNode(31, -19));

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);
        main.addNeighbor(friend3);

        // The searched featured
        Search searchResult = main.discover(-7);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(8, searchResult.getBandwidth());
        Assert.assertEquals(23, searchResult.getNode().getId());
    }

    @Test()
    /* Should fail if immediate relationship does not contain
    *  the relationship AND their immediate relationship does not as well.
    */
    public void discoverDeepRelationshipTest2Fail() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his 1 friend
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        friend1.addNeighbor(getFeatureNode(10, -16));

        // Friend 2 and his 4 friends
        DecentralizedNode friend2 = getFeatureNode(2, -5, -2);
        friend2.addNeighbor(getFeatureNode(20, -9));
        friend2.addNeighbor(getFeatureNode(21, -18));
        friend2.addNeighbor(getFeatureNode(23, -7));
        friend2.addNeighbor(getFeatureNode(24, -2));

        // Friend 3 and his 2 friend
        DecentralizedNode friend3 = getFeatureNode(3, -5, -1);
        friend3.addNeighbor(getFeatureNode(30,-3));
        friend3.addNeighbor(getFeatureNode(31, -19));

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);
        main.addNeighbor(friend3);

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
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -6);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100, -7));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111,-3));

        // Friend 2 and his nested relationship
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -6);
        DecentralizedNode f2f2 = getFeatureNode(21, -16);

        friend2.addNeighbor(f2f1);
        f2f1.addNeighbor(getFeatureNode(200, -7));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        friend2.addNeighbor(f2f2);
        f2f2.addNeighbor(getFeatureNode(210, -8));
        f2f2.addNeighbor(getFeatureNode(211,-3));


        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        // The searched featured
        Search searchResult = main.discover(-8);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(14, searchResult.getBandwidth());
        Assert.assertEquals(210, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node in a deep nested relationships
    *  and the feature appear at depth = 1 and 3;
    */
    public void discoverDeepDeepRelationshipTest3Pass() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -6);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100,-3));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111,-3));

        // Friend 2 and his nested relationship
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -999);  // feature appear at depth 2
        DecentralizedNode f2f2 = getFeatureNode(21, -16);

        friend2.addNeighbor(f2f1);
        f2f1.addNeighbor(getFeatureNode(200, -999));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        friend2.addNeighbor(f2f2);
        f2f2.addNeighbor(getFeatureNode(210, -8));
        f2f2.addNeighbor(getFeatureNode(211,-3));


        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(6, searchResult.getBandwidth());
        Assert.assertEquals(20, searchResult.getNode().getId());
    }

    @Test()
    /* Test for featur at depth = 4. */
    public void discoverDepth4Test() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -6);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100,-3));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111,-3));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -6);  // feature appear at depth 2
        DecentralizedNode f2f2 = getFeatureNode(21, -16);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -1);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -999); // feature appear here

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

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
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -999);  // feature appear here

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100,-3));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -999));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -6);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -1);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -999);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        // The searched featured
        Search searchResult = main.discover(-999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(5, searchResult.getBandwidth());
        Assert.assertEquals(11, searchResult.getNode().getId());
    }

    @Test()
    /* Test for feature that doesn't appear in network, search up to depth 4 */
    public void discoverDepth4Test2Fail() {
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -12);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100,-3));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -14));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -6);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -1);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -9);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

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
        DecentralizedNode main = getFeatureNode(0, -15);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -1, -2);
        DecentralizedNode f1f1 = getFeatureNode(10, -6);
        DecentralizedNode f1f2 = getFeatureNode(11, -999);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100,-3));
        f1f1.addNeighbor(getFeatureNode(101,-3));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -999));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -1, -2);
        DecentralizedNode f2f1 = getFeatureNode(20, -6);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -2));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211,-3);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -999);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -999);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

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


    public DecentralizedNode getFeatureNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (DecentralizedNode) mf.create(NodeType.DECENTRAL, id, Role.BOTH, TimeToLive.HIGH, features);
    }

}
