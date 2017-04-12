package models.history;

import models.Feature;
import models.Search;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * A stack of HistoryLogs. Represent
 * a collection of pass discovery results
 */
public class History extends ArrayDeque<Search> {
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
    public void push(Search search) {
        if (this.size() < maxSize) {
            super.push(search);
        } else {
            this.pop();
            this.push(search);
        }
    }

    /**
     * Check if there a history search with this feature
     */
    public Search contains(Feature feature) {
        Iterator<Search> it = this.iterator();
        Search search = null;

        while (it.hasNext()) {
            Search itSearch = it.next();
            if (itSearch.getFeature().equals(feature)) {
                search = itSearch;
                break;
            }
        }

        if (search == null) {
            return null;
        } else {
            this.remove(search);
            this.push(search);
            return search;
        }
    }
}
