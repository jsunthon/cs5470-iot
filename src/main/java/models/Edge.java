package models;

public class Edge {
    private Node src;
    private Node dest;
    private Relationship relationship;

    public Edge(Node src, Node dest, Relationship relationship) {
        this.src = src;
        this.dest = dest;
        this.relationship = relationship;
    }

    public Node getDest() {
        return dest;
    }

    public Node getSrc() {
        return src;
    }
}
