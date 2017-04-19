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
        friend1.addRelationship(getFeatureNode(10, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(20, Feature.randomFeature()), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 3 and his 1 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(30, Feature.randomFeature()), Relationship.FAMILY);

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
        friend1.addRelationship(getFeatureNode(10, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(20, Feature.randomFeature()), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, Feature.randomFeature()), Relationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(30, Feature.randomFeature()), Relationship.FAMILY);
        friend3.addRelationship(getFeatureNode(31, Feature.randomFeature()), Relationship.CO_WORKER);

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
        friend1.addRelationship(getFeatureNode(10, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 2 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(20, Feature.WATER_LEAKAGE), Relationship.FAMILY);
        friend2.addRelationship(getFeatureNode(21, Feature.SILOS_STOCK_MEASUREMENT), Relationship.FAMILY);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(30, Feature.PORTABLE_WATER_MONITORING), Relationship.FAMILY);
        friend3.addRelationship(getFeatureNode(31, Feature.SWIMMING_POOL_MONITOR), Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend2, Relationship.FAMILY.FRIEND);
        main.addRelationship(friend3, Relationship.FAMILY.FRIEND);

        Search searchResult = main.discover(Feature.SILOS_STOCK_MEASUREMENT);
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
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(10, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(20, Feature.WATER_LEAKAGE), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(21, Feature.SILOS_STOCK_MEASUREMENT), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(23, Feature.PHOTOVOLTAIC_MONITOR), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(24, Feature.TRAFFIC_CONGESTION), Relationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(30, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(31, Feature.SWIMMING_POOL_MONITOR), Relationship.FRIEND);

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
        Assert.assertEquals(23, searchResult.getNode().getId());
    }

    @Test()
    /* Should fail if immediate relationship does not contain
    *  the relationship AND their immediate relationship does not as well.
    */
    public void discoverDeepRelationshipTest2Fail() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his 1 friend
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        friend1.addRelationship(getFeatureNode(10, Feature.TANK_LEVEL_MONITOR), Relationship.FAMILY);

        // Friend 2 and his 4 friends
        SocialNode friend2 = getFeatureNode(2, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend2.addRelationship(getFeatureNode(20, Feature.WATER_LEAKAGE), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(21, Feature.SILOS_STOCK_MEASUREMENT), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(23, Feature.PHOTOVOLTAIC_MONITOR), Relationship.CO_WORKER);
        friend2.addRelationship(getFeatureNode(24, Feature.TRAFFIC_CONGESTION), Relationship.CO_WORKER);

        // Friend 3 and his 2 friend
        SocialNode friend3 = getFeatureNode(3, Feature.STRUCTURAL_HEALTH, Feature.TRAFFIC_CONGESTION);
        friend3.addRelationship(getFeatureNode(30, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        friend3.addRelationship(getFeatureNode(31, Feature.SWIMMING_POOL_MONITOR), Relationship.FRIEND);

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

    @Test()
    /* Should return true in a deep nested relationships */
    public void discoverDeepDeepRelationshipTest2Pass() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.ELECTROMAGNETIC_FIELD_LEVELS);

        friend1.addRelationship(f1f1, Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, Feature.PORTABLE_WATER_MONITORING), Relationship.FRIEND);

        // Friend 2 and his nested relationship
        SocialNode friend2 = getFeatureNode(2, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f2f1 = getFeatureNode(20, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f2f2 = getFeatureNode(21, Feature.TANK_LEVEL_MONITOR);

        friend2.addRelationship(f2f1, Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(200, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, Feature.SMART_PARKING), Relationship.ACQUAINTANCE);

        friend2.addRelationship(f2f2, Relationship.CO_WORKER);
        f2f2.addRelationship(getFeatureNode(210, Feature.SMARTPHONE_DETECTION), Relationship.ACQUAINTANCE);
        f2f2.addRelationship(getFeatureNode(211, Feature.PORTABLE_WATER_MONITORING), Relationship.FRIEND);


        // Add friends to main
        main.addRelationship(friend1, Relationship.FRIEND);
        main.addRelationship(friend2, Relationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.SMARTPHONE_DETECTION);
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
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.ELECTROMAGNETIC_FIELD_LEVELS);

        friend1.addRelationship(f1f1, Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, Feature.PORTABLE_WATER_MONITORING), Relationship.FRIEND);

        // Friend 2 and his nested relationship
        SocialNode friend2 = getFeatureNode(2, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f2f1 = getFeatureNode(20, Feature.TRAFFIC_CONGESTION);  // feature appear at depth 2
        SocialNode f2f2 = getFeatureNode(21, Feature.TANK_LEVEL_MONITOR);

        friend2.addRelationship(f2f1, Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(200, Feature.TRAFFIC_CONGESTION), Relationship.ACQUAINTANCE); // feature also appear at depth 3
        f2f1.addRelationship(getFeatureNode(201, Feature.SMART_PARKING), Relationship.ACQUAINTANCE);

        friend2.addRelationship(f2f2, Relationship.CO_WORKER);
        f2f2.addRelationship(getFeatureNode(210, Feature.SMARTPHONE_DETECTION), Relationship.ACQUAINTANCE);
        f2f2.addRelationship(getFeatureNode(211, Feature.PORTABLE_WATER_MONITORING), Relationship.FRIEND);


        // Add friends to main
        main.addRelationship(friend1, Relationship.FRIEND);
        main.addRelationship(friend2, Relationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.TRAFFIC_CONGESTION);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertTrue(searchResult.isSuccess());
        Assert.assertEquals(4, searchResult.getBandwidth());
        Assert.assertEquals(20, searchResult.getNode().getId());
    }

    @Test()
    /* Test for featur at depth = 4. */
    public void discoverDepth4Test() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.ELECTROMAGNETIC_FIELD_LEVELS);

        friend1.addRelationship(f1f1, Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, Feature.PORTABLE_WATER_MONITORING), Relationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f2f1 = getFeatureNode(20, Feature.ELECTROMAGNETIC_FIELD_LEVELS);  // feature appear at depth 2
        SocialNode f2f2 = getFeatureNode(21, Feature.TANK_LEVEL_MONITOR);

        // depth = 2
        friend2.addRelationship(f2f1, Relationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, Relationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, Feature.SMART_PARKING), Relationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, Feature.SMARTPHONE_DETECTION);
        SocialNode f2f2f2 = getFeatureNode(211, Feature.PORTABLE_WATER_MONITORING);

        // depth = 3
        f2f2.addRelationship(f2f2f1, Relationship.FRIEND);
        f2f2.addRelationship(f2f2f2, Relationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, Feature.SMART_LIGHTING);
        SocialNode f2f2f2f2 = getFeatureNode(21101, Feature.TRAFFIC_CONGESTION); // feature appear here

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, Relationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FRIEND);
        main.addRelationship(friend2, Relationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.TRAFFIC_CONGESTION);
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
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.TRAFFIC_CONGESTION);  // feature appear here

        friend1.addRelationship(f1f1, Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, Feature.TRAFFIC_CONGESTION), Relationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f2f1 = getFeatureNode(20, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f2f2 = getFeatureNode(21, Feature.SMARTPHONE_DETECTION);

        // depth = 2
        friend2.addRelationship(f2f1, Relationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, Relationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, Feature.SMART_PARKING), Relationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, Feature.SMARTPHONE_DETECTION);
        SocialNode f2f2f2 = getFeatureNode(211, Feature.PORTABLE_WATER_MONITORING);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, Relationship.FRIEND);
        f2f2.addRelationship(f2f2f2, Relationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, Feature.SMART_LIGHTING);
        SocialNode f2f2f2f2 = getFeatureNode(21101, Feature.TRAFFIC_CONGESTION);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, Relationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FRIEND);
        main.addRelationship(friend2, Relationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.TRAFFIC_CONGESTION);
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
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.ENERGY_MONITOR);

        friend1.addRelationship(f1f1, Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(100, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);
        f1f1.addRelationship(getFeatureNode(101, Feature.PORTABLE_WATER_MONITORING), Relationship.ACQUAINTANCE);

        friend1.addRelationship(f1f2, Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(110, Feature.PHOTOVOLTAIC_MONITOR), Relationship.ACQUAINTANCE);
        f1f2.addRelationship(getFeatureNode(111, Feature.RIVER_FLOODS), Relationship.FRIEND);

        // Friend 2 and his nested relationship
        // depth = 1
        SocialNode friend2 = getFeatureNode(2, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f2f1 = getFeatureNode(20, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f2f2 = getFeatureNode(21, Feature.SMARTPHONE_DETECTION);

        // depth = 2
        friend2.addRelationship(f2f1, Relationship.ACQUAINTANCE);
        friend2.addRelationship(f2f2, Relationship.CO_WORKER);

        // depth = 3
        f2f1.addRelationship(getFeatureNode(200, Feature.WATER_LEAKAGE), Relationship.ACQUAINTANCE);
        f2f1.addRelationship(getFeatureNode(201, Feature.SMART_PARKING), Relationship.ACQUAINTANCE);

        SocialNode f2f2f1 = getFeatureNode(210, Feature.SMARTPHONE_DETECTION);
        SocialNode f2f2f2 = getFeatureNode(211, Feature.PORTABLE_WATER_MONITORING);

        // depth = 3, again
        f2f2.addRelationship(f2f2f1, Relationship.FRIEND);
        f2f2.addRelationship(f2f2f2, Relationship.CO_WORKER);

        SocialNode f2f2f2f1 = getFeatureNode(21100, Feature.SMART_LIGHTING);
        SocialNode f2f2f2f2 = getFeatureNode(21101, Feature.WATER_LEAKAGE);

        // depth = 4
        f2f2f2.addRelationship(f2f2f2f1, Relationship.ACQUAINTANCE);
        f2f2f2.addRelationship(f2f2f2f2, Relationship.CO_WORKER);

        // Add friends to main
        main.addRelationship(friend1, Relationship.FRIEND);
        main.addRelationship(friend2, Relationship.FRIEND);

        // The searched featured
        Search searchResult = main.discover(Feature.TRAFFIC_CONGESTION);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(null, searchResult.getNode());
    }
    /* ******************************************* */
    /* *************** SET UP DATA *************** */
    /* ******************************************* */


    public SocialNode getFeatureNode(int id, Feature... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return mf.create(id, Role.BOTH, TimeToLive.HIGH, features);
    }


}
