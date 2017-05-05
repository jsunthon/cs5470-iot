const Node = require('./nodeJsonGenerator').Node;

const initial_nodes = 10;
const max_nodes     = 2000;
const nodes         = initNodes();
let total_edges     = 0;

connect();

while (nodes.length < max_nodes) {
  createNode();
}

const data = {};

nodes.forEach(n => {
  const count = n.relationships.length;
  if(data[count]) {
    data[count]++;
  } else {
    data[count] = 1;
  }
});
console.log(data);

//
// Connect nodes to every other nodes
function connect() {
  for (let i = 0; i < nodes.length; i++) {
    for (let k = 0; k < nodes.length; k++) {
      const nodeI = nodes[i];
      const nodeK = nodes[k];

      if (i !== k) {
        nodeI.addRelationship(nodeK, -2);
        total_edges += 2;
      }
    }
  }
}

function initNodes() {
  const ns = [];

  for (let i = 0; i < initial_nodes; i++) {
    ns.push(new Node(i, -1));
  }

  return ns;
}

function pProb(node) {
  const n = node.relationships.length;
  const d = total_edges;
  return n / d;
}

function createNode() {
  console.log(nodes.length);
  const newNode = new Node(nodes.length + 1, -1);
  const rNode   = randomNode();


  const r = rProb();
  const p = pProb(rNode);

  if (p > r) {
    newNode.addRelationship(rNode, -1);
    nodes.push(newNode);
    total_edges += 2;
  }
}

function randomNode() {
  const randomIndex = random(0, nodes.length - 1);
  return nodes[randomIndex];
}

function random(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function rProb() {
  return Math.random();
}