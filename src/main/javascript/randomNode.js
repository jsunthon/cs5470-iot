const fs = require('fs');

class Node {

  constructor(id, feature) {
    this.id = id;
    /* A number represent an IOT feature,
     * e.g. RAIN_MEASUREMENT, STEP_TRACKER, etc... */
    this.feature = feature;
    /* Arrays of node's id, easier  to deal with a primitive value then object */
    this.relationships = [];
  }

  /* type is a number (1-4) and it refers to
   * (ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND) */
  addRelationship(node, type) {
    if (this.relationships.indexOf(node.id) === -1 && node.id !== this.id) {
      this.relationships.push({id: node.id, feature: node.feature, type: type});
    }
  }

}
/* *********************************************************************** */
/* ***********************  CONFIGURABLE OPTIONS ************************* */
/* *********************************************************************** */
const NUM_OF_NODES = 20000;

/* These two values are used to generate the number of
 * relationship a node can have, but does not include relationship
 * added due to reciprocity. (All relationship are two way) */
const LOWER_RELATIONSHIP = 1;
const UPPER_RELATIONSHIP = 10;

const MAX_FEATURES = 2000;

/* The number of features that a single feature can be group with it
 * e.g. feature 2 tends to have nodes with feature 5, 6, 7, and 8;
 */
const RELATED_FEATURES = 6;

/* The chance that a relationship on a node will contain the same feature */
const FEATURE_RELATIONSHIP_CHANCE = .10;

const NODES = [];

/* {feature: [array of features]}. Each feature tends to be group with
 * the array of features. */
const FEATURE_INDEX = featureIndex();

/* *********************************************************************** */
/* ****************************  MAIN ************************************ */
/* *********************************************************************** */

console.log('-------------------- feature index -------------------- ');
console.log(FEATURE_INDEX);
console.log('-----------------------------------------------------');

generateNodes();
generateRelationship();
writeToFile('nodes.json');
displayRelationshipStatus();


/* *********************************************************************** */
/* *****************************  FUNCTIONS  ***************************** */
/* *********************************************************************** */
function generateNodes() {
  for (let i = 1; i <= NUM_OF_NODES; i++) {
    const node = new Node(i, randomFeature());
    console.log(node.id);
    NODES.push(node);
  }
}

function generateRelationship() {
  for (let node of NODES) {
    // Determine the number of relationship this node should have
    const numbOfRelationships = random(LOWER_RELATIONSHIP, UPPER_RELATIONSHIP);

    for (let i = 0; i < numbOfRelationships; i++) {
      const relationship = randomRelationship(node);
      addRelationship(node, relationship);
    }
  }
}

function writeToFile(filename) {
  const json = {NODES, FEATURE_INDEX};
  fs.writeFile(filename, JSON.stringify(json, null, 2), 'utf8', () => {
    console.log('done');
  })
}

/**
 * Returns an object that display the max, min, total, avg relationship of NODES
 */
function displayRelationshipStatus() {
  /* Calculate nodes highest/smallest relationship length, */
  const obj = NODES.reduce((accumulator, node, index) => {
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

function random(min, max, exclude) {
  let value = Math.floor(Math.random() * (max - min + 1)) + min;
  if (!exclude) return value;

  while (value === exclude) {
    value = Math.floor(Math.random() * (max - min + 1)) + min;
  }
  return value;
}

function randomElement(array) {
  return array[random(0, array.length - 1)];
}

function randomFeature() {
  return random(1, MAX_FEATURES);
}

function randomRelationship(node) {
  if (Math.random() <= FEATURE_RELATIONSHIP_CHANCE) {
    const byFeature  = getNodesByFeature(NODES, node.feature);
    let relationship = randomElement(byFeature);
    return relationship;
  }
  else {
    let featureGroup       = FEATURE_INDEX[node.feature];
    let randomFeature      = randomElement(featureGroup);
    let nodesWithFeature   = getNodesByFeature(NODES, randomFeature);
    let randomRelationship = randomElement(nodesWithFeature);
    return randomRelationship;
  }
}

function addRelationship(node1, node2) {
  node1.addRelationship(node2, relationshipType(1, 4));
  node2.addRelationship(node1, relationshipType(1, 4));
}

function relationshipType() {
  return random(1, 4);
}

function getNodesByFeature(nodes, feature) {
  return nodes.filter(node => node.feature === feature)
}

function featureIndex() {
  const result = {};
  for (let currentFeature = 1; currentFeature <= MAX_FEATURES; currentFeature++) {
    result[currentFeature] = [];
    for (let i = 0; i < RELATED_FEATURES; i++) {
      const r = random(1, MAX_FEATURES);
      result[currentFeature].push(r);
    }
  }
  return result;
}