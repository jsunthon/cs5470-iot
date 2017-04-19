package models;

/* TODO: add more features */
public enum Feature {

    SMART_PARKING,
    STRUCTURAL_HEALTH,
    NOISE,
    SMARTPHONE_DETECTION,
    ELECTROMAGNETIC_FIELD_LEVELS,
    TRAFFIC_CONGESTION,
    SMART_LIGHTING,
    SMART_ROAD,
    WASTE_MANAGEMENT,

    // Smart Water
    PORTABLE_WATER_MONITORING,
    RIVER_FLOODS,
    WATER_LEAKAGE,
    SEA_POLLUTION_LEVELS,
    SWIMMING_POOL_MONITOR,
    RIVER_CHEMICAL_LEAKAGE_DETECTION,

    // Smart metering
    ENERGY_MONITOR,
    TANK_LEVEL_MONITOR,
    PHOTOVOLTAIC_MONITOR,
    WATER_FLOW_MONITOR,
    SILOS_STOCK_MEASUREMENT;


    public static Feature randomFeature() {
        int randInd = (int) (Math.random() * Feature.values().length);
        return Feature.values()[randInd];
    }
}
