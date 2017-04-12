package models.history;

import models.Feature;
import models.nodes.SocialNode;

import java.util.Date;

/**
 * HistoryLog represent the result of a single Node's discovery
 * search.
 */
public class HistoryLog {
    /*  The feature that a Node searched for */
    private Feature feature;

    /* The result Node from the discovery search.
      Null if search yield no result */
    private SocialNode node;

    /*  The date of the search. */
    private Date dateSearch;


    public HistoryLog(Feature feature, SocialNode node) {
        this.feature = feature;
        this.node = node;
        this.dateSearch = new Date();
    }

    public Feature getFeature() {
        return feature;
    }

    public SocialNode getNode() {
        return node;
    }

    public Date getDateSearch() {
        return dateSearch;
    }


}
