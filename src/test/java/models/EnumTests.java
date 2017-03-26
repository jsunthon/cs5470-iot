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
    public void TestRandomRole() {
        Assert.assertNotNull(Role.randomRole());
    }

    @Test
    public void TestRandomTimeToLive() {
        Assert.assertNotNull(TimeToLive.randomTimeToLive());
    }
}
