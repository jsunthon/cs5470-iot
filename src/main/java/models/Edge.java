package models;

import models.nodes.Node;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Edge {
    private Node src;
    private Node dest;
    private int ms;

    // Each edge, relationship, will be a random value between min and max
    private static final int MIN_MS = 10;
    private static final int MAX_MS = 50;

    public Edge(Node src, Node dest) {
        this.src = src;
        this.dest = dest;
        this.ms = randomMs();
    }

    public Node getSrc() {
        return src;
    }

    public Node getDest() {
        return dest;
    }

    public int getMs() {
        return ms;
    }

    private int randomMs() {
        // Add one to make MAX_MS "inclusive"
        return ThreadLocalRandom.current().nextInt(MIN_MS, MAX_MS + 1);
    }

    public String toString() {
        return "{"
                + "src:" + src.getId() + ","
                + "dest:" + dest.getId() + ","
                + "ms:" + ms + ","
                + "}";
    }
}
