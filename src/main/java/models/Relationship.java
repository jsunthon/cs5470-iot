package models;

public enum Relationship {

    CO_WORKER, FAMILY, FRIEND, ACQUAINTANCE;

    public static Relationship randomRelationship() {
        int randInd = (int) Math.random() * Relationship.values().length;
        return Relationship.values() [randInd];
    }
}
