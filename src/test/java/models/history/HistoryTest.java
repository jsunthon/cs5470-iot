package models.history;

import models.*;
import models.nodes.SocialNode;

import org.junit.Assert;
import org.junit.Test;

public class HistoryTest {
    @Test
    public void testHistoryPushPop() {
        HistoryLog log = HistoryTest.getTestLog();
        History history = new History(2);
        history.push(log);
        Assert.assertEquals(log, history.pop());
    }

    @Test
    public void testMaxSize() {
        History history = new History(2);
        Assert.assertEquals(2, history.getMaxSize());
    }

    @Test
    public void testEmptyHistorySize() {
        History history = new History(2);
        Assert.assertEquals(0, history.size());
    }

    @Test
    public void testSingleHistorySize() {
        History history = new History(2);
        history.push(getTestLog());
        Assert.assertEquals(1, history.size());
    }

    @Test
    public void testExceedHistoryMaxSize() {
        History history = new History(2);
        history.push(getTestLog());
        history.push(getTestLog());
        history.push(getTestLog());
        Assert.assertEquals(2, history.size());
    }


    private static HistoryLog getTestLog() {
        Feature feature = Feature.randomFeature();
        SocialNode node = getTestNode();
        return new HistoryLog(feature, node);
    }

    private static SocialNode getTestNode() {
        return new SocialNode(1, getTestManufacture(), Role.BOTH, TimeToLive.LOW);
    }

    private static Manufacturer getTestManufacture() {
        return new Manufacturer("Blue");
    }
}
