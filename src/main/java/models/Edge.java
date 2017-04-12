package models;

import models.nodes.SocialNode;

public class Edge {
    private SocialNode src;
    private SocialNode dest;
    private Relationship relationship;

    public Edge(SocialNode src, SocialNode dest, Relationship relationship) {
        this.src = src;
        this.dest = dest;
        this.relationship = relationship;
    }

    public SocialNode getDest() {
        return dest;
    }

    public SocialNode getSrc() {
        return src;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public int getDiversityScore() {
        return dest.getRelationshipMap().size();
    }
}
