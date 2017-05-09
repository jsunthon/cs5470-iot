const Node = require('./nodeJsonGenerator').Node;
const fs   = require('fs');

const config = {
  FILE_NAME            : 'node-50k-feat-25k.json',
  INITIAL_NODES        : 10,
  NUM_OF_NODES         : 50000,
  // These two values are used to generate the number of
  // relationship a node can have, but does not include relationship
  // added due to reciprocity. (All relationship are two way)
  LOWER_RELATIONSHIP   : 1,
  UPPER_RELATIONSHIP   : 10,
  NUM_FEATURES         : 25000,
// ACQUAINTANCE, CO_WORKER, FAMILY, FRIEND
  NUM_RELATIONSHIP_TYPE: 4,
// How much features a node can have.
  NUM_FEATURES_PER_NODE: 2,
};

const nodes    = initNodes();
let totalEdges = 0;


function generate() {
  setupInitConnected();

  while (nodes.length < config.NUM_OF_NODES) {
    createNode();
  }
}

function displayEdgeCount() {
  const data = {};
  nodes.forEach(n => {
    const count = n.relationships.length;
    if (data[count]) {
      data[count]++;
    } else {
      data[count] = 1;
    }
  });
  console.log(Object.keys(data).map(d => parseInt(d)));
  console.log(Object.values(data));
  console.log(data);
}


// Connect nodes to every other nodes
function setupInitConnected() {
  for (let i = 0; i < nodes.length; i++) {
    for (let k = 0; k < nodes.length; k++) {
      const nodeI = nodes[i];
      const nodeK = nodes[k];

      if (i !== k) {
        nodeI.addRelationship(nodeK, random(1, config.NUM_RELATIONSHIP_TYPE));
        totalEdges += 2;
        break;
      }
    }
  }
}

function initNodes() {
  const ns = [];
  for (let i = 0; i < config.INITIAL_NODES; i++) {
    const features = randomFeatures(2, config.NUM_FEATURES);
    ns.push(new Node((i + 1), features));
  }
  return ns;
}

function pProb(node) {
  const n = node.relationships.length;
  const d = totalEdges;
  return n / d;
}

function createNode() {
  let edgeAdded      = 0;
  let numOfEdgeToAdd = random(config.LOWER_RELATIONSHIP, config.UPPER_RELATIONSHIP);

  for (let i = 0; i < nodes.length; i++) {
    const nodeI = nodes[i];
    const r     = Math.random();
    const p     = pProb(nodeI);

    if (r <= p) {
      const features = randomFeatures(config.NUM_FEATURES_PER_NODE, config.NUM_FEATURES);
      const newNode  = new Node(nodes.length + 1, features);
      newNode.addRelationship(nodeI, random(1, config.NUM_RELATIONSHIP_TYPE));
      nodes.push(newNode);

      console.log('nodes created: ', nodes.length);

      totalEdges += 2;
      edgeAdded++;

      if (edgeAdded === numOfEdgeToAdd) {
        break;
      }
    }
  }
}

function random(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

/** Return a random array of features */
function randomFeatures(lengthOfArray, numOfFeatures) {
  const features = new Set();
  for (let i = 0; i < lengthOfArray; i++) {
    features.add(randomFeature(numOfFeatures));
  }
  return Array.from(features);
}

/** Return a random feature from 1 to input value */
function randomFeature(numOfFeatures) {
  return random(1, numOfFeatures);
}

function writeToFile(fileName) {
  generate();
  displayEdgeCount();

  const json = {config, nodes};
  fs.writeFile(fileName, JSON.stringify(json, null, 2), 'utf8', () => {
    console.log('done');
  });

}

writeToFile(config.FILE_NAME);
