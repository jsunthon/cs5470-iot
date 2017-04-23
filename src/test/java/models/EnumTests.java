package models;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jsunthon on 3/25/2017.
 */
public class EnumTests {
    @Test
    public void TestGetRandomFeature() {
        Assert.assertNotNull(Feature.randomFeature());
    }

    @Test
    public void TestRandomRelationship() {
        Assert.assertNotNull(Relationship.randomRelationship());
    }
    
    @Test
    public void TestTargetRelationship() {
    	Relationship relationship1 = Relationship.getRelationship(1);
    	Assert.assertEquals(Relationship.ACQUAINTANCE, relationship1);
    	Relationship relationship2 = Relationship.getRelationship(2);
    	Assert.assertEquals(Relationship.CO_WORKER,  relationship2);
    	Relationship relationship3 = Relationship.getRelationship(3);
    	Assert.assertEquals(Relationship.FAMILY, relationship3);
    	Relationship relationship4 = Relationship.getRelationship(4);
    	Assert.assertEquals(Relationship.FRIEND, relationship4);
    }

    @Test
    public void TestRandomRole() {
        Assert.assertNotNull(Role.randomRole());
    }

    @Test
    public void TestRandomTimeToLive() {
        Assert.assertNotNull(TimeToLive.randomTimeToLive());
    }
}
