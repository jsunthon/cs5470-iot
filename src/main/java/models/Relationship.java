package models;

import models.nodes.SocialNode;


public class Relationship extends Edge {
    private SocialRelationship socialRelationship;

    public Relationship(SocialNode src, SocialNode dest, SocialRelationship socialRelationship) {
        super(src, dest);
        this.socialRelationship = socialRelationship;
    }

    public SocialNode getDest() {
        return (SocialNode) super.getDest();
    }

    public SocialNode getSrc() {
        return (SocialNode) super.getSrc();
    }

    public SocialRelationship getSocialRelationship() {
        return socialRelationship;
    }

    public int getDiversityScore() {
        return this.getDest().getRelationshipMap().size();
    }

    public String toString() {
        return "{"
                + "src:" + getSrc().getId() + ","
                + "dest:" + getDest().getId() + ","
                + "ms:" + getMs() + ","
                + "rel:" + socialRelationship + ","
                + "cen:" + getDest().getCentrality() + ","
                + "div:" + getDiversityScore()
                + "}";
    }
}
