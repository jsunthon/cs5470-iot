package models;

public enum SocialRelationship {

    ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND, ;

    public static SocialRelationship randomRelationship() {
        int randInd = (int) (Math.random() * SocialRelationship.values().length);
        return SocialRelationship.values() [randInd];
    }
    
    /**
     * Returns a relationship located at index - 1, since relationship integers
     * generated from the JSON value is not 0-based indexed.
     * @param index
     * @return
     */
    public static SocialRelationship getRelationship(int index) {
    	return SocialRelationship.values()[--index];
    }
}
