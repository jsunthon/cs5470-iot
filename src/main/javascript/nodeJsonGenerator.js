const fs = require('fs');

class Node {

  constructor(id, features) {
    this.id = id;

    // A number represent an IOT feature,
    // e.g. RAIN_MEASUREMENT, STEP_TRACKER, etc...
    if (features.length === 0) {
      this.features = [];
    } else {
      this.features = features;
    }

    // Arrays of node's id, easier  to deal with a primitive value then object
    this.relationships = [];
  }

  /**
   * Add node to relationship. Return true if added, false otherwise.
   * type is a number (1-4) and it refers to.
   */
  addRelationship(node, type) {
    if (!this.hasRelationship(node) && node.id !== this.id) {
      this.relationships.push({
        id      : node.id,
        features: node.features,
        type    : type
      });

      // Every relationship is two way
      node.addRelationship(this, type);
    }
  }

  /** Return true if the node is not contain in this relationship */
  hasRelationship(node) {
    return this.relationships.findIndex(relation => {
          return relation.id === node.id;
        }) !== -1;
  }

}
/* *********************************************************************** */
/* ***********************  CONFIGURABLE OPTIONS ************************* */
/* *********************************************************************** */
const NUM_OF_NODES = 20000;

// These two values are used to generate the number of
// relationship a node can have, but does not include relationship
// added due to reciprocity. (All relationship are two way)
const LOWER_RELATIONSHIP = 1;
const UPPER_RELATIONSHIP = 10;

const NUM_FEATURES = 1000;

// The number of features that a single feature can be group with it
// e.g. feature 2 tends to have nodes with feature 5, 6, 7, and 8;
const FEATURE_GROUP_SIZE = 6;

// The chance that a relationship on a node will contain the same feature */
const FEATURE_RELATIONSHIP_CHANCE = .10;

// ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND
const NUM_RELATIONSHIP_TYPE = 4;

// How much features a node can have.
const NUM_FEATURES_PER_NODE = 2;

/* *********************************************************************** */
/* ****************************  MAIN  ************************************ */
/* *********************************************************************** */


function writeToFile(fileName) {
  const featureObj = featureIndex(NUM_FEATURES, FEATURE_GROUP_SIZE);

  console.log('-------------------- feature index -------------------- ');
  console.log(featureObj);
  console.log('-----------------------------------------------------');

  const nodes = randomNodes(NUM_OF_NODES, NUM_FEATURES);
  generateRelationship(nodes, FEATURE_RELATIONSHIP_CHANCE, featureObj);

  const json = {nodes}
  fs.writeFile(fileName, JSON.stringify(json, null, 2), 'utf8', () => {
    console.log('done');
  })

  displayRelationshipStatus(nodes);
}

/* *********************************************************************** */
/* *****************************  FUNCTIONS  ***************************** */
/* *********************************************************************** */
/** Return a list of random nodes (with random featurs) */
function randomNodes(numOfNodes, numOfFeatures) {
  const nodes = [];
  for (let i = 1; i <= numOfNodes; i++) {
    const features = randomFeatures(NUM_FEATURES_PER_NODE, NUM_FEATURES)
    const node     = new Node(i, features);
    nodes.push(node);
  }
  return nodes;
}

/** Add a random relationship to each node based on how each feature
 * agre group (featureObj) */
function generateRelationship(nodes, sameFeatureChance, featureObj) {
  for (let node of nodes) {
    // Determine the number of relationship this node should have
    const numbOfRelationships = random(LOWER_RELATIONSHIP, UPPER_RELATIONSHIP);

    for (let i = 0; i < numbOfRelationships; i++) {
      const relationship = randomRelationship(node.features, sameFeatureChance, nodes, featureObj);
      if (relationship) {
        node.addRelationship(relationship, randomRelationshipType(NUM_RELATIONSHIP_TYPE));
      }
    }
    console.log('done:', node.id);
  }
}

function randomFeatures(featuresPerArray, numOfFeatures) {
  const features = new Set();
  for (let i = 0; i < featuresPerArray; i++) {
    features.add(randomFeature(numOfFeatures));
  }
  return Array.from(features);
}
/**
 * Returns an object that display the max, min, total, avg relationship of NODES
 */
function displayRelationshipStatus(nodes) {
  // Calculate nodes highest/smallest relationship length
  const obj = nodes.reduce((accumulator, node, index) => {
    const max   = node.relationships.length > accumulator.max
        ? node.relationships.length : accumulator.max;
    const min   = node.relationships.length < accumulator.min
        ? node.relationships.length : accumulator.min;
    const total = accumulator.total + node.relationships.length;
    const avg   = total / (index + 1);
    let maxNode = accumulator.maxNode;

    if (maxNode) {
      maxNode = maxNode.relationships.length >= node.relationships.length ? maxNode : node;
    } else {
      maxNode = node;
    }
    return {max, min, total, avg, maxNode};
  }, {max: -Infinity, min: Infinity, total: 0, maxNode: null});
  console.log('obj', obj);
}

/** Return a random value between MIN and MAX (inclusive) */
function random(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

/** Return a random element from an array.
 * If the array is empty, return null */
function randomElement(array) {
  if (!array || array.length === 0) {
    //  Can't return random element of an empty list, could throw an error here
    return null;
  } else {
    return array[random(0, array.length - 1)];
  }
}

/** Return a random feature from 1 to input value */
function randomFeature(numOfFeatures) {
  return random(1, numOfFeatures);
}
function randomRelationship(features, sameFeatureChance, nodes, featureObject) {
  if (Math.random() <= sameFeatureChance) {
    return randomElement(
        getNodesByFeatures(nodes, features));
  }
  else {
    let featureArray = [];
    for (let feature of features) {
      featureArray = featureArray.concat(featureObject[feature]);
    }
    // Remove duplicates from featureArray
    featureArray = Array.from(new Set(featureArray));

    return randomNodeByFeatureArray(featureArray, nodes);
  }
}

/** Return a random node with a random feature based on a list of feature.
 * May return null if no node in <nodes> contain the randomFeature*/
function randomNodeByFeatureArray(featureArray, nodes) {
  let features        = [randomElement(featureArray)];
  let nodesByFeature  = getNodesByFeatures(nodes, features);
  let nodeWithFeature = randomElement(nodesByFeature);
  return nodeWithFeature;
}

/** Return a random relationship type (1-4) */
function randomRelationshipType(numOfRelationship) {
  return random(1, numOfRelationship);
}

/** Return a list of nodes that contain have at least one of feature
 * in <features> */
function getNodesByFeatures(nodes, features) {
  return nodes.filter(node => {
    return node.features.some(feature => {
      return features.indexOf(feature) > -1;
    })
  });
}


function randomNodeSameFeature(feature, nodes) {
  if (nodes.length === 0) {
    console.error('Cannot return random node with the same feature if nodes is empty');
    throw 'Operation on Empty List Exception';
  }

  const nodesByFeature = getNodesByFeatures(nodes, feature);
  return randomElement(nodesByFeature);
}

/** Return an object where the keys are FEATURE value (1,2,3)
 * and the property are ARRAYS of FEATURES [5,6,7].
 * Each FEATURE tends to have relationship with the ARRAYS of FEATURES */
function featureIndex(numOfFeatures, featureGroupSize) {
  const featureObject = {};

  for (let featureKey = 1; featureKey <= numOfFeatures; featureKey++) {
    const featureSet = new Set();

    for (let i = 0; i < featureGroupSize; i++) {
      const randomFeature = random(1, numOfFeatures);
      featureSet.add(randomFeature);
    }
    featureObject[featureKey] = Array.from(featureSet);
  }
  return featureObject;
}

module.exports = {
  Node,
  random,
  randomElement,
  randomFeature,
  randomFeatures,
  randomNodes,
  randomNodeByFeatureArray,
  generateRelationship,
  featureIndex,
  getNodesByFeatures,

  writeToFile
}
