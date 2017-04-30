package models.socialNode;


import models.*;
import models.nodes.NodeType;
import models.nodes.SocialNode;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTestById {
    @Test()
    /* Test for feature that occur at depth = 4  and depth = 2.
    * Should return the node that appear in depth= 2;
    */
    public void discoverDepth4Test2IdPass() {
        SocialNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -2, -3);
        SocialNode f1f1 = getFeatureNode(10, -4);
        SocialNode f1f2 = getFeatureNode(11, -5);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, -6), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, -6), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -5), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -2, -3);
        SocialNode f2f1 = getFeatureNode(20, -4);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -3), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -2);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -5);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discoverById(21101);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(21101, searchResult.getNode().getId());
    }

    @Test()
    /* Test for feature that doesn't appear in network, search up to depth 4 */
    public void discoverDepth4Test2IdFail() {
        SocialNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -2, -3);
        SocialNode f1f1 = getFeatureNode(10, -4);
        SocialNode f1f2 = getFeatureNode(11, -10);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, -6), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, -6), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -11), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -2, -3);
        SocialNode f2f1 = getFeatureNode(20, -4);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -3), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -2);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -9);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discoverById(9999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(null, searchResult.getNode());
    }

    @Test()
    public void discoverDepth4Test2IDPass2() {
        SocialNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, -2, -3);
        SocialNode f1f1 = getFeatureNode(10, -4);
        SocialNode f1f2 = getFeatureNode(11, -5);

        friend1.addRelationship(f1f1, SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, -6), SocialRelationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, -6), SocialRelationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, -7), SocialRelationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, -5), SocialRelationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, -2, -3);
        SocialNode f2f1 = getFeatureNode(20, -4);
        SocialNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addRelationship(f2f1, SocialRelationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, SocialRelationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, -9), SocialRelationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, -3), SocialRelationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, -8);
        SocialNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, SocialRelationship.FRIEND);
        f2f2.addRelationship(f2f2f2, SocialRelationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, -2);
        SocialNode f2f2f2f2 = getFeatureNode(21101, -5);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, SocialRelationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, SocialRelationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, SocialRelationship.FRIEND);
        main.addRelationship(friend2, SocialRelationship.FRIEND);

        // The searched featured
        Search searchResult = main.discoverById(11);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(7, searchResult.getBandwidth());
        Assert.assertEquals(11, searchResult.getNode().getId());
    }

    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */


    public SocialNode getFeatureNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (SocialNode) mf.create(NodeType.SOCIAL, id, Role.BOTH, TimeToLive.HIGH, features);
    }
}
