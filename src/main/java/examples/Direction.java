package examples;

public enum Direction {
    NORTH(20),              // Value inside are cost
    EAST(40),
    SOUTH(60),
    WEST(80);

    private final int cost; // Declare cost here

    Direction(int cost) {   // To assign a value for the enum,
        this.cost = cost;   // set up constructor like so
    }

    // An enum instance method
    public int getCost() {
        return this.cost;
    }

    // An enum static method!
    public static Direction randomDirection() {
        int index = (int) (Math.random() * Direction.values().length);

        // Enum.values() returns  NORTH, EAST, etc..
        return Direction.values()[index];
    }


    /**
     * Return name of the enum (e.g. NORTH) with an exclamation mark
     */
    public String exclaimName() {
        return this.toString() + "!";
    }


}


