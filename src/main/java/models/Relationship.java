package models;

public enum Relationship {

    ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND, ;

    public static Relationship randomRelationship() {
        int randInd = (int) (Math.random() * Relationship.values().length);
        return Relationship.values() [randInd];
    }
}
