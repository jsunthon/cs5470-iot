const writer = require('./nodeJsonGenerator');
const config = writer.config;

config.NUM_OF_NODES = 20000;

// These two values are used to generate the number of
// relationship a node can have, but does not include relationship
// added due to reciprocity. (All relationship are two way)
config.LOWER_RELATIONSHIP = 1;
config.UPPER_RELATIONSHIP = 10;

config.NUM_FEATURES = 6000;

// The number of features that a single feature can be group with it
// e.g. feature 2 tends to have nodes with feature 5, 6, 7, and 8;
config.FEATURE_GROUP_SIZE = 6;

// The chance that a relationship on a node will contain the same feature */
config.FEATURE_RELATIONSHIP_CHANCE = .10;

// ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND
config.NUM_RELATIONSHIP_TYPE = 4;

// How much features a node can have.
config.NUM_FEATURES_PER_NODE = 2;


//--------------------------------------------------
//------------------------- main --------------------
writer.writeToFile('node-20k-feat-6k.json');
