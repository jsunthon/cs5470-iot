package models;

public enum TimeToLive {
    LOW, MEDIUM, HIGH;

    public static TimeToLive randomTimeToLive() {
        int randInd = (int) Math.random() * TimeToLive.values().length;
        return TimeToLive.values() [randInd];
    }
}