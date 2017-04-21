const fs = require('fs');

class Node {

  constructor(id, feature) {
    this.id = id;
    /* A number represent an IOT feature,
     * e.g. RAIN_MEASUREMENT, STEP_TRRACKER, etc... */
    this.feature = feature;
    /* Arrays of node's id, eaiser  to deal with a primitive value then object */
    this.relationships = [];
  }

  /* type is a number (1-4) and it refers to
   * (ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND) */
  addRelationship(node, type) {
    if (this.relationships.indexOf(node.id) === -1 && node.id !== this.id) {
      this.relationships.push({id: node.id, feature: node.feature, type: type});
    }
  }

  hasRelationship(node) {
    return this.relationships.some(edge => {
      return edge.id === node.id || node.id == this.id;
    })
  }
}
/* *********************************************************************** */
/* ***********************  CONFIGURABLE OPTIONS ************************* */
/* *********************************************************************** */
const NUM_OF_NODES = 1000;

/* These two values are used to generate the number of
 * relationship a node can have, but does not include relationship
 * added due to reciprocality. (All relationship are two way) */
const LOWER_RELATIONSHIP = 5;
const UPPER_RELATIONSHP  = 10;

const MAX_FEATURES = 100;

/* The chance that a relationshp on a node will contain the same feature */
const FEATURE_RELATIONSHP_CHANCE = .25;

const NODES = [];

/* *********************************************************************** */
/* ****************************  MAIN ************************************ */
/* *********************************************************************** */


generateNodes();
generateRelationship();
writeToFile('nodes.json');
// displayRelationshpStatus();


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
    const numOfRelationshp = random(LOWER_RELATIONSHIP, UPPER_RELATIONSHP);

    for (let i = 0; i < numOfRelationshp; i++) {
      const relationship = randomRelationship(node);
      addRelationshp(node, relationship);
    }
  }
}

function writeToFile(filename) {
  const json = {NODES};
  fs.writeFile(filename, JSON.stringify(json, null, 2), 'utf8', () => {
    console.log('done');
  })
}

function displayRelationshpStatus() {
  /* Calculate nodes highest/smallest relationship length, */
  const obj = NODES.reduce((accumulator, node) => {
    const max = node.relationships.length > accumulator.max
        ? node.relationship.length : accumulator.max;
    const min = node.relationships.length < accumulator.min
        ? node.relationships.length : accumulator.min;
    return {max, min}
  }, {max: -Infinity, min: Infinity})
  console.log('obj', obj);
}
function random(min, max) {
  return Math.floor(Math.random() * (max + min - 1) + min);
}

function randomNode(nodes) {
  return nodes[random(0, nodes.length)];
}

function randomFeature() {
  return random(1, MAX_FEATURES);
}

function randomRelationship(node) {

  if (Math.random() <= .60) {
    const byFeature  = getNodesByFeature(NODES, node.feature);
    let relationship = randomNode(byFeature);
    return relationship;
  }
  else {
    let relationshipId = random(1, NUM_OF_NODES);
    while (relationshipId === node.id) {
      relationshipId = random(1, NUM_OF_NODES);
    }
    return NODES[relationshipId - 1];
  }
}

function addRelationshp(node1, node2) {
  node1.addRelationship(node2, relationshipType(1, 4));
  node2.addRelationship(node1, relationshipType(1, 4));
}

function relationshipType() {
  return random(1, 4);
}

function getNodesByFeature(nodes, feature) {
  return nodes.filter(node => node.feature === feature)
}