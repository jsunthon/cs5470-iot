package examples;

import org.junit.Assert;
import org.junit.Test;

public class DirectionTests {

    @Test
    public void TestRandomDirection() {
        Assert.assertNotNull(Direction.randomDirection());
    }

    @Test
    public void TestGetCost() {
        Assert.assertEquals(Direction.NORTH.getCost(), 20);
        Assert.assertEquals(Direction.EAST.getCost(), 40);
        Assert.assertEquals(Direction.SOUTH.getCost(), 60);
        Assert.assertEquals(Direction.WEST.getCost(), 80);
    }


    @Test
    public void TestExclaimName() {
        Assert.assertEquals(Direction.NORTH.exclaimName(), "NORTH!");
    }
}

