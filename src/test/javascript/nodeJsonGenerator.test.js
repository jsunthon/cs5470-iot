const expect    = require('chai').expect;
const generator = require('../../main/javascript/nodeJsonGenerator');
const Node      = generator.Node;
describe("RandomNode", () => {

  describe('Node Class', () => {
    let node;
    let other;
    beforeEach(() => {
      node  = new Node(1, 10);
      other = new Node(2, 8);
    });


    it('should not be able to add itself to relationship', () => {
      node.addRelationship(node, 1);
      expect(node.relationships).to.be.lengthOf(0);
    });

    it('should add unique node onto relationship', () => {
      node.addRelationship(other, 1);
      expect(node.relationships).to.be.lengthOf(1);
      expect(other.relationships).to.be.lengthOf(1);
    });

    it('should not added same node into relationship', () => {
      node.addRelationship(other, 1);
      node.addRelationship(other, 2);
      node.addRelationship(other, 1);
      expect(node.relationships).to.be.lengthOf(1);
      expect(other.relationships).to.be.lengthOf(1);
    });
  });

  it('should return a number between 3 and 5', () => {
    const min    = 3, max = 5;
    const result = generator.random(min, max);
    for (let i = 0; i < 100; i++) {
      expect(result).to.be.least(min);
      expect(result).to.be.at.most(max);
    }
  });

  describe('randomElement', () => {
    it('should return an element from the array', () => {
      const xs     = [1, 2];
      const result = generator.randomElement(xs);
      for (let i = 0; i < 100; i++) {
        expect(result).to.be.ok;
      }
    });
  });

  it('should return a random feature between 1 and 3', () => {
    const NUM_FEATURES = 3;
    const result       = generator.randomFeature(NUM_FEATURES);
    for (let i = 0; i < 100; i++) {
      expect(result).to.be.at.least(1);
      expect(result).to.be.at.most(NUM_FEATURES);
    }
  });

  describe('featureIndex()', () => {
    const NUM_FEATURES = 3;
    const GROUP_SIZE   = 2;
    let featureObject;

    beforeEach(() => {
      featureObject = generator.featureIndex(NUM_FEATURES, GROUP_SIZE);
    });

    it('should be a defined object', () => {
      expect(featureObject).to.be.not.undefined;
      expect(featureObject).to.be.ok;
    });

    it('should contain number KEYS', () => {
      expect(featureObject).to.have.all.keys(['1', '2', '3']);
    });

    it('values array\s length should =  GROUP_SIZE', () => {
      for (let values of Object.values(featureObject)) {
        expect(values.length).to.be.at.most(GROUP_SIZE);

        for (let value of values) {
          expect(value).to.be.at.most(NUM_FEATURES);
          expect(value).to.be.at.least(1);
        }
      }
    })
  });

  describe('getNodeByFeature', () => {
    let nodes;

    beforeEach(() => {
      nodes = [];
      nodes.push(new Node(1, [1]));
      nodes.push(new Node(2, [1]));
      nodes.push(new Node(3, [2]));
    });

    it('should return nodes with the same feature', () => {
      expect(generator.getNodesByFeatures(nodes, [1])).to.be.lengthOf(2);
      expect(generator.getNodesByFeatures(nodes, [1, 2])).to.be.lengthOf(3);
      expect(generator.getNodesByFeatures(nodes, [1, 2, 3])).to.be.lengthOf(3);
      expect(generator.getNodesByFeatures(nodes, [3])).to.be.lengthOf(0);
    });
  });

  describe('randomNodes', () => {
    it('should return a list of Node object', () => {
      const randomNodes = generator.randomNodes(3);
      expect(randomNodes).to.be.lengthOf(3);

      for (let node of randomNodes) {
        expect(node).to.have.all.keys('id', 'features', 'relationships');
      }
    });
  });

  describe('randomNodeByFeatureArray', () => {
    let nodes;
    let featureArray;

    beforeEach(() => {
      nodes        = [new Node(1, [1]), new Node(1, [1]), new Node(1, [2])];
      featureArray = [1, 2, 3, 4];
    });

    it('should return a node', () => {
      for (let i = 0; i < 10; i++) {
        const node = generator.randomNodeByFeatureArray(featureArray, nodes);
        if (node) {
          expect(node).has.keys('id', 'features', 'relationships');
        }
      }
    });

  });

  describe('randomFeatures', () => {
    it('should return an array of features', () => {
      const features = generator.randomFeatures(3, 5);
      expect(features.length).to.be.at.most(3);

      features.forEach(feature => {
        expect(feature).to.be.at.least(1);
        expect(feature).to.be.at.most(5);
      })
    });
  });


  describe('generateRelationship', () => {
    let nodes;
    let featureObj;

    beforeEach(() => {
      nodes = [new Node(1, [1]), new Node(2, [1]), new Node(3, [2])]
    });

    it('should not throw an error ', () => {
      featureObj = {'1': [2], '2': [1]};
      for (let i = 0; i < 100; i++) {
        nodes = [new Node(1, [1]), new Node(2, [1]), new Node(3, [2])];
        generator.generateRelationship(nodes, .10, featureObj);
      }

    });

    it('should now throw an error when no nodes have the feature', () => {
      featureObj = {'1': [2], '2': [5, 6]};
      for (let i = 0; i < 100; i++) {
        nodes = [new Node(1, [1]), new Node(2, [1, 2]), new Node(3, [1, 2, 3])];
        generator.generateRelationship(nodes, .10, featureObj);
      }
    });
  });

});
