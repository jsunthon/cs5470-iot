package models;

/* TODO: add more features */
public enum Feature {

    SMART_PARKING,
    STRCTURAL_HEALTH,
    NOISE,
    SMARTPHONE_DECTECTION,
    ELETRONMAGENTIC_FIELD_LEVELS,
    TRAFFIC_CONGESTION,
    SMART_LIGHTING,
    WASTE_MANAG,
    SMART_ROAD,
    WASTE_MANAGEMENT;

    public static Feature randomFeature() {
        int randInd = (int) (Math.random() * Feature.values().length);
        return Feature.values()[randInd];
    }
}
