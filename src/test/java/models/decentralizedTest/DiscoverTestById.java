package models.decentralizedTest;


import models.Manufacturer;
import models.Role;
import models.Search;
import models.TimeToLive;
import models.nodes.DecentralizedNode;
import models.nodes.NodeType;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTestById {
    @Test()
    // Find node that is in at depth=4
    public void discoverDepth4Test2IdPass() {
        DecentralizedNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -2, -3);
        DecentralizedNode f1f1 = getFeatureNode(10, -4);
        DecentralizedNode f1f2 = getFeatureNode(11, -5);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100, -6));
        f1f1.addNeighbor(getFeatureNode(101, -6));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -5));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -2, -3);
        DecentralizedNode f2f1 = getFeatureNode(20, -4);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -3));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -2);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -5);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

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
        DecentralizedNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -2, -3);
        DecentralizedNode f1f1 = getFeatureNode(10, -4);
        DecentralizedNode f1f2 = getFeatureNode(11, -10);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100, -6));
        f1f1.addNeighbor(getFeatureNode(101, -6));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -11));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -2, -3);
        DecentralizedNode f2f1 = getFeatureNode(20, -4);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -3));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -2);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -9);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

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
        DecentralizedNode main = getFeatureNode(0, -1);

        // Friend 1 and his nested relationship
        DecentralizedNode friend1 = getFeatureNode(1, -2, -3);
        DecentralizedNode f1f1 = getFeatureNode(10, -4);
        DecentralizedNode f1f2 = getFeatureNode(11, -5);

        friend1.addNeighbor(f1f1);
        f1f1.addNeighbor(getFeatureNode(100, -6));
        f1f1.addNeighbor(getFeatureNode(101, -6));

        friend1.addNeighbor(f1f2);
        f1f2.addNeighbor(getFeatureNode(110, -7));
        f1f2.addNeighbor(getFeatureNode(111, -5));

        // Friend 2 and his nested relationship
        // depth = 1
        DecentralizedNode friend2 = getFeatureNode(2, -2, -3);
        DecentralizedNode f2f1 = getFeatureNode(20, -4);
        DecentralizedNode f2f2 = getFeatureNode(21, -8);

        // depth = 2
        friend2.addNeighbor(f2f1);
        friend2.addNeighbor(f2f2);

        // depth = 3
        f2f1.addNeighbor(getFeatureNode(200, -9));
        f2f1.addNeighbor(getFeatureNode(201, -3));

        DecentralizedNode f2f2f1 = getFeatureNode(210, -8);
        DecentralizedNode f2f2f2 = getFeatureNode(211, -6);

        // depth = 3, again
        f2f2.addNeighbor(f2f2f1);
        f2f2.addNeighbor(f2f2f2);

        DecentralizedNode f2f2f2f1 = getFeatureNode(21100, -2);
        DecentralizedNode f2f2f2f2 = getFeatureNode(21101, -5);

        // depth = 4
        f2f2f2.addNeighbor(f2f2f2f1);
        f2f2f2.addNeighbor(f2f2f2f2);

        // Add friends to main
        main.addNeighbor(friend1);
        main.addNeighbor(friend2);

        // The searched featured
        Search searchResult = main.discoverById(11);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(5, searchResult.getBandwidth());
        Assert.assertEquals(11, searchResult.getNode().getId());
    }

    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */


    public DecentralizedNode getFeatureNode(int id, Integer... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return (DecentralizedNode) mf.create(NodeType.DECENTRAL, id, Role.BOTH, TimeToLive.HIGH, features);
    }
}
