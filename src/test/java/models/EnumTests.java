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
        Assert.assertNotNull(SocialRelationship.randomRelationship());
    }
    
    @Test
    public void TestTargetRelationship() {
    	SocialRelationship socialRelationship1 = SocialRelationship.getRelationship(1);
    	Assert.assertEquals(SocialRelationship.ACQUAINTANCE, socialRelationship1);
    	SocialRelationship socialRelationship2 = SocialRelationship.getRelationship(2);
    	Assert.assertEquals(SocialRelationship.CO_WORKER, socialRelationship2);
    	SocialRelationship socialRelationship3 = SocialRelationship.getRelationship(3);
    	Assert.assertEquals(SocialRelationship.FAMILY, socialRelationship3);
    	SocialRelationship socialRelationship4 = SocialRelationship.getRelationship(4);
    	Assert.assertEquals(SocialRelationship.FRIEND, socialRelationship4);
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
