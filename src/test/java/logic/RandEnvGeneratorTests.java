package logic;
import models.Manufacturer;
import models.Owner;
import models.nodes.NodeType;
import models.nodes.SocialNode;

import org.junit.Assert;
import org.junit.Test;

import logic.RandEnvGenerator;

import java.util.List;

/**
 * Created by jsunthon on 3/25/2017.
 */
public class RandEnvGeneratorTests {

    @Test
    public void testGenRandNumber() {
        int featureCount = RandEnvGenerator.genRandNumber(1, 3);
        Assert.assertTrue(featureCount >= 1 && featureCount <= 3);
        featureCount = RandEnvGenerator.genRandNumber(20, 100);
        Assert.assertTrue(featureCount >= 20 && featureCount <= 100);
    }

    @Test
    public void testGenManufacturers() {
        RandEnvGenerator randEnvGenerator = RandEnvGenerator.getInstance();
        randEnvGenerator.reset();
        List<Manufacturer> manufacturers = randEnvGenerator.genManufacturers(7, 7);
        Assert.assertNotNull(manufacturers);
        Assert.assertEquals(7, manufacturers.size());
    }

    @Test
    public void testGenOwners() {
    	RandEnvGenerator randEnvGenerator = RandEnvGenerator.getInstance();
    	randEnvGenerator.reset();
        List<Owner> owners = randEnvGenerator.genOwners(100);
        Assert.assertNotNull(owners);
        Assert.assertEquals(100, owners.size());
    }

    @Test
    public void testGenRandomizeNode() {
    	RandEnvGenerator randEnvGenerator = RandEnvGenerator.getInstance();
    	randEnvGenerator.reset();
        Assert.assertNull(randEnvGenerator.genRandomizeNode(NodeType.SOCIAL, 1, 3));
        randEnvGenerator.genManufacturers(10, 5);
        randEnvGenerator.genOwners(100);
        SocialNode node = (SocialNode) randEnvGenerator.genRandomizeNode(NodeType.SOCIAL, 2, 3);
        Assert.assertNotNull(node);
        Assert.assertEquals(1, randEnvGenerator.getNodes().size());
        node = (SocialNode) randEnvGenerator.genRandomizeNode(NodeType.SOCIAL, 4, 5);
        Assert.assertEquals(2, randEnvGenerator.getNodes().size());
    }
}
