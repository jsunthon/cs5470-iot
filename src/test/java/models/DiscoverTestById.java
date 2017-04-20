package models;


import models.nodes.SocialNode;
import org.junit.Assert;
import org.junit.Test;

public class DiscoverTestById {
    @Test()
    /* Test for feature that occur at depth = 4  and depth = 2.
    * Should return the node that appear in depth= 2;
    */
    public void discoverDepth4Test2IdPass() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.TRAFFIC_CONGESTION);

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
        Search searchResult = main.discover(21101);
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
        Search searchResult = main.discover(9999);
        System.out.println(searchResult);
        System.out.println(searchResult.getNodeVisited());

        // Tests
        Assert.assertFalse(searchResult.isSuccess());
        Assert.assertEquals(17, searchResult.getBandwidth());
        Assert.assertEquals(null, searchResult.getNode());
    }

    @Test()
    public void discoverDepth4Test2IDPass2() {
        SocialNode main = getFeatureNode(0, Feature.NOISE);

        // Friend 1 and his nested relationship
        SocialNode friend1 = getFeatureNode(1, Feature.SMART_LIGHTING, Feature.SMART_PARKING);
        SocialNode f1f1 = getFeatureNode(10, Feature.ELECTROMAGNETIC_FIELD_LEVELS);
        SocialNode f1f2 = getFeatureNode(11, Feature.TRAFFIC_CONGESTION);

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
        Search searchResult = main.discover(11);
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


    public SocialNode getFeatureNode(int id, Feature... features) {
        Manufacturer mf = new Manufacturer("test-mf");
        return mf.create(id, Role.BOTH, TimeToLive.HIGH, features);
    }
}
