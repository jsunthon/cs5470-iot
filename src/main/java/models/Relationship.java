package models;

public enum Relationship {

    ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND, ;

    public static Relationship randomRelationship() {
        int randInd = (int) (Math.random() * Relationship.values().length);
        return Relationship.values() [randInd];
    }
    
    /**
     * Returns a relationship located at index - 1, since relationship integers
     * generated from the JSON value is not 0-based indexed.
     * @param index
     * @return
     */
    public static Relationship getRelationship(int index) {
    	return Relationship.values()[--index];
    }
}
