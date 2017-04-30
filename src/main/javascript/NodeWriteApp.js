const yargs  = require('yargs');
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
const flags = yargs.usage("Nodes Generator")
    .options('n', {
      alias   : 'nodes',
      describe: 'number of nodes to generate',
      choices : [20000, 50000],
      require : true
    })
    .options('f', {
      alias   : 'features',
      describe: 'number of features to generate',
      choices : [
        2000, 4000, 6000, 8000, 10000,
        5000, 10000, 15000, 20000, 25000],
      require : true
    })
    .options('t', {
      alias   : 'title',
      describe: 'filename (e.g. nodes-20k-feat-2k)',
      require : true
    })
    .argv;

if (flags['f'] < flags['n']) {
  config.NUM_OF_NODES = flags['nodes'];
  config.NUM_FEATURES = flags['features'];

  writer.writeToFile(`${flags['title']}.json`)
}
