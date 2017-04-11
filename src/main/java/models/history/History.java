package models.history;

import models.Feature;
import models.nodes.SocialNode;

import java.util.Stack;

/**
 * A stack of HistoryLogs. Represent
 * a collection of pass discovery results
 */
public class History extends Stack<HistoryLog> {
    private int maxSize;

    public History(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Add a Log into History Stack but if the amount
     * of items in the stack exceeds the maxSize,
     * then pop (remove) the older logs;
     */
    @Override
    public HistoryLog push(HistoryLog item) {
        if (this.size() < maxSize) {
            return super.push(item);
        } else {
            this.pop();
            return this.push(item);
        }
    }

    public HistoryLog push(Feature feature, SocialNode node) {
        return this.push(new HistoryLog(feature, node));
    }
}
