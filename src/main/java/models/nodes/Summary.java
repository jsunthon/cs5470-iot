package models.nodes;

import java.util.HashMap;
import java.util.Map;

public class Summary {
    public int                  numberOfSuccess     = 0;
    public int                  totalLatency        = 0;
    public int                  totalNodesContacted = 0;
    public int                  totalNumber         = 0;
    public Map<String, Integer> hopsCounter         = new HashMap<>();


    public String type;

    public Summary(String type) {
        this.type = type;
    }

    private double getSuccessRate() {
        return (numberOfSuccess * 1.0) / totalNumber;
    }

    private double getAvgLatency() {
        return (totalLatency * 1.0) / totalNumber;
    }

    private double getAvgNodesContacted() {
        return (totalNodesContacted * 1.0) / totalNumber;
    }

    public String toString() {
        return "{" +
                "type:" + type + "," +
                "successRate:" + getSuccessRate() + "," +
                "avgLatency:" + getAvgLatency() + "," +
                "avgNodeContacted:" + getAvgNodesContacted() + "," +
                "hopCounter: " + hopsCounter + "," +
                "}";
    }
}
