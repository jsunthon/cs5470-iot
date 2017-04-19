package models;

import models.nodes.Node;
import models.nodes.SocialNode;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTests {

    @Test()
    /* Test if node returns itself in the result if it already have the feature
    * to discover */
    public void discoverTestSelfPass() {
        Node main = getFeatureNode(0, Feature.NOISE);
        Search searchResult = main.discover(Feature.NOISE);

        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(1, searchResult.getBandwidth());
        Assert.assertEquals(0, searchResult.getNode().getId());
    }

    @Test()
    /* Test if node return unsuccessful search if it has no relationship and does
    * not have the feature. */
    public void discoverTestSelfFail() {
        Node main = getFeatureNode(0, Feature.NOISE);
        Search searchResult = main.discover(Feature.ELECTROMAGNETIC_FIELD_LEVELS);

        System.out.println(searchResult);

        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(1, searchResult.getBandwidth());
    }


    @Test()
    /* Test if node 2 immediate relationship contains the feature.
     * Should return  true */
    public void discoverTestImmediateRelationship1() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Relationships
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.SMART_LIGHTING);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
    }

    @Test()
    /* Should return false if the two immediate relationships does not
    * contains the feature */
    public void discoverTestImmediateRelationship2() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Relationships
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.WASTE_MANAGEMENT);
        System.out.println(searchResult);

        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(3, searchResult.getBandwidth());
    }


    @Test()
    /* Should check the node with the higher centrality of the node's
     * three immediate relationships FIRST to see if it
     * contains the features */
    public void discoverTestImmediateRelationship3() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(100, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(200, Feature.randomFeature()), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(201, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 3 and his 1 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(300, Feature.randomFeature()), Relationship.FAMILY);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.STRUCTURAL_HEALTH);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
        Assert.assertEquals(2, searchResult.getNode().getId());
    }

    @Test()
    /* Should check the node with the higher diversity score
     * when the centrality are equal of the node's
     * three immediate relationships FIRST to see if it
     * contains the features */
    public void discoverTestImmediateRelationship4() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(100, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(200, Feature.randomFeature()), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(201, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(300, Feature.randomFeature()), Relationship.FAMILY);
        friend3.addRelationship(getFeatureNode(301, Feature.randomFeature()), Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.STRUCTURAL_HEALTH);
        System.out.println(searchResult);

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(2, searchResult.getBandwidth());
        Assert.assertEquals(3, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node if immediate relationship does not contain
    *  the relationship but THEIR immediate relationship does. */
    public void discoverDeepRelationshipTest1() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(100, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(201, Feature.SILOS_STOCK_MEASUREMENT), Relationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(300, Feature.PORTABLE_WATER_MONITORING), Relationship.FAMILY);
        friend3.addRelationship(getFeatureNode(301, Feature.SWIMMING_POOL_MONITOR), Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.SILOS_STOCK_MEASUREMENT);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(8, searchResult.getBandwidth());
        Assert.assertEquals(201, searchResult.getNode().getId());
    }

    @Test()
    /* Should return true/right node if immediate relationship does not contain
    *  the relationship but THEIR immediate relationship does.
    *
    * Test if discover prioritize nodes with higher centrality.
    * Friend2 has lower diversity, but higher centrality, should prioritize it
    */
    public void discoverDeepRelationshipTest2() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(100, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(201, Feature.SILOS_STOCK_MEASUREMENT), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(203, Feature.PHOTOVOLTAIC_MONITOR), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(204, Feature.TRAFFIC_CONGESTION), Relationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(300, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(301, Feature.SWIMMING_POOL_MONITOR), Relationship.FRIEND);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.PHOTOVOLTAIC_MONITOR);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(7, searchResult.getBandwidth());
        Assert.assertEquals(203, searchResult.getNode().getId());
    }

    @Test()
    /* Should fail if immediate relationship does not contain
    *  the relationship AND their immediate relationship does not as well.
    */
    public void discoverDeepRelationshipTest2Fail() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(100, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(201, Feature.SILOS_STOCK_MEASUREMENT), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(203, Feature.PHOTOVOLTAIC_MONITOR), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(204, Feature.TRAFFIC_CONGESTION), Relationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(300, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(301, Feature.SWIMMING_POOL_MONITOR), Relationship.FRIEND);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.SEA_POLLUTION_LEVELS);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(11, searchResult.getBandwidth());
        Assert.assertNull(searchResult.getNode());
    }
    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */


    public SocialNode getFeatureNode(int id, Feature... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return mf.create(id, Role.BOTH, TimeToLive.HIGH, features);
    }


}
