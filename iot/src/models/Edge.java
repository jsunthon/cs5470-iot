package models;

public class Edge {
    private Node node1;
    private Node node2;
    private Relationship relationship;

    public Edge(Node node1, Node node2) {}

    public Edge(Node node1, Node node2, Relationship relationship) {}

    public Node getNode2() {
        return node2;
    }
}
