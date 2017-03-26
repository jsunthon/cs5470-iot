package models;

public enum Role {
    SENDER, RECEIVER, BOTH;

    public static Role randomRole() {
        int randInd = (int) Math.random() * Role.values().length;
        return Role.values() [randInd];
    }
}