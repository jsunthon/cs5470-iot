package logic;

import models.*;
import models.nodes.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Parser {
    private static final Integer NODE_FEATURE_COUNT = 3;
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private Node[] socialNodes;
    private Node[] centralNodes;
    private Node[] decentralNodes;
    private RandEnvGenerator randEnvGen;
    private Set<Integer> usedFeatures;
    private JSONParser jsonParser;

    private List<Integer> featureList;
    private List<Integer> randomFeatureList;

    public Parser() {
        randEnvGen = RandEnvGenerator.getInstance();
        jsonParser = new JSONParser();
        usedFeatures = new HashSet<>();

        featureList = new ArrayList<>();
        randomFeatureList = new ArrayList<>();
    }

    /**
     * Generates the social topology from a json file
     *
     * @param path
     */
    public void parseAndGenSocial(String path) {
        try {
            FileReader fileReader = new FileReader(path);
            Object object = jsonParser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get("nodes");
            initNodeArrays(jsonArray.size());
            Iterator<JSONObject> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonNode = iterator.next();
                Long longId = (Long) jsonNode.get("id"); //json-simple parses numbers as Longs and not ints...
                int id = new Integer(longId.intValue());
                JSONArray features = (JSONArray) jsonNode.get("features");
                JSONArray relationships = (JSONArray) jsonNode.get("relationships");
                Iterator<JSONObject> relationsIterator = relationships.iterator();
                socialNodes[id] = genSocialNode(id, features, relationsIterator);
            }

            // Centrality and diversity score of each edge is not
            // accurate after parsing, quick fix (hack solution)
            sortByRelationshipDiversity();
        } catch (IOException | ParseException e) {
            logger.error(e.getMessage());
        }
    }

    public void initNodeArrays(int length) {
        socialNodes = new Node[length + 1];
        centralNodes = new Node[length + 1];
        decentralNodes = new Node[length + 1];
    }

    /**
     * Returns true if the central topology could be generated from social topology
     *
     * @return
     */
    public boolean genCentral() {
        if (socialNodes.length > 1 && socialNodes.length == centralNodes.length) {
            //start at i = 1 since a node's id can only be as low as 1.
            for (int i = 1; i < centralNodes.length; i++) {
                SocialNode socialNode = (SocialNode) socialNodes[i];
                if (socialNode == null) return false;
                if (i == 1) {
                    //node at index 1 will be MasterNode. All nodes at other indexes are slave nodes
                    //maybe not the best practice. will have to make sure that we perform
                    //checks for casting any node at index 1 in centralNodes to be MasterNode
                    centralNodes[i] = (MasterNode) genNodeFromSocial(socialNode, NodeType.MASTER);
                } else {
                    //master node always the first node, aka node with id = 1
                    MasterNode masterNode = (MasterNode) centralNodes[1];
                    SlaveNode slaveNode = (SlaveNode) genNodeFromSocial(socialNode, NodeType.SLAVE);
                    slaveNode.setMaster(masterNode);
                    centralNodes[i] = slaveNode;
                    masterNode.addSlaveNode(slaveNode);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates the decentral node topology from existing social node topology.
     *
     * @return
     */
    public boolean genDecentral() {
        if (socialNodes.length > 1 && socialNodes.length == decentralNodes.length) {
            for (int i = 1; i < decentralNodes.length; i++) {
                SocialNode socialSrc = (SocialNode) socialNodes[i];
                if (socialSrc == null) return false;
                DecentralizedNode decenSrc = (DecentralizedNode) getOrCreateDecenNode(socialSrc);
                List<Edge> shuffledEdges = shuffleSortedEdges(socialSrc.getSortedEdges());
                for (Edge relationEdge : shuffledEdges) {
                    SocialNode socialNeighbor = relationEdge.getDest();
                    DecentralizedNode decenNeighbor = (DecentralizedNode) getOrCreateDecenNode(socialNeighbor);
                    decenSrc.addNeighbor(decenNeighbor);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes a set of edges, and shuffles them to make sure they're not longer sorted.
     *
     * @param sortedEdges
     * @return
     */
    public List<Edge> shuffleSortedEdges(Set<Edge> sortedEdges) {
        List<Edge> unshuffledEdges = new ArrayList<>();
        unshuffledEdges.addAll(sortedEdges);
        Collections.shuffle(unshuffledEdges);
        return unshuffledEdges;
    }

    /**
     * Created a node of nodeType that shares the same properties as the socialNode
     * passed in. These properties are id, share, time to live, role, manufacturer, and owner
     *
     * @param socialNode
     * @param nodeType
     * @return
     */
    public Node genNodeFromSocial(SocialNode socialNode, NodeType nodeType) {
        if (socialNode == null) return null;
        int id = socialNode.getId();
        TimeToLive ttl = socialNode.getTimeToLive();
        Role role = socialNode.getRole();
        Set<Integer> featureSet = socialNode.getFeatures();
        Integer[] featureArr = featureSet.toArray(new Integer[featureSet.size()]);
        boolean share = socialNode.getShare();
        Owner owner = socialNode.getOwner();
        Manufacturer manuf = socialNode.getManufacturer();
        Node createdNode = null;
        switch (nodeType) {
            case MASTER:
                createdNode = (MasterNode) manuf.create(NodeType.MASTER, id, role, ttl, featureArr);
                break;
            case SLAVE:
                createdNode = (SlaveNode) manuf.create(NodeType.SLAVE, id, role, ttl, featureArr);
                break;
            case DECENTRAL:
                createdNode = (DecentralizedNode) manuf.create(NodeType.DECENTRAL, id, role, ttl, featureArr);
                break;
        }
        if (createdNode != null) {
            createdNode.setOwner(owner);
            createdNode.setShare(share);
        }
        return createdNode;
    }

    /**
     * Generate a social node with relationships
     *
     * @param id
     * @param relationships
     * @return
     */
    public SocialNode genSocialNode(int id, JSONArray featuresArr, Iterator<JSONObject> relationships) {
        Integer[] features = featureJsonToArr(featuresArr);
        for (int feature : features) {
            usedFeatures.add(feature);
        }
        SocialNode srcNode = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, id, features);
        while (relationships.hasNext()) {
            JSONObject jsonNode = relationships.next();
            Long longId = (Long) jsonNode.get("id");
            int intId = longId.intValue();
            JSONArray neighborFeatJson = (JSONArray) jsonNode.get("features");
            Integer[] neighborFeatArr = featureJsonToArr(neighborFeatJson);
            Long longType = (Long) jsonNode.get("type");
            int intType = longType.intValue();
            SocialNode neighbor = (SocialNode) getOrCreateNode(socialNodes, NodeType.SOCIAL, intId, neighborFeatArr);
            srcNode.addRelationship(neighbor, Relationship.getRelationship(intType));
        }
        return srcNode;
    }

    /**
     * Gets a node from the list of nodes, creating the node with random props if it doesn't already exist.
     * Also adds created node to the passed in node array.
     *
     * @param nodes
     * @param nodeType
     * @param id
     * @return
     */
    public Node getOrCreateNode(Node[] nodes, NodeType nodeType, int id, Integer[] features) {
        Node node = nodes[id];
        if (node == null) {
            node = randEnvGen.genRandomizeNode(nodeType, id, features);
            nodes[id] = node;
        }
        return node;
    }

    public Node getOrCreateDecenNode(SocialNode socialNode) {
        int decenNodeIndex = socialNode.getId();
        Node node = decentralNodes[decenNodeIndex];
        if (node == null) {
            node = genNodeFromSocial(socialNode, NodeType.DECENTRAL);
            decentralNodes[decenNodeIndex] = node;
        }
        return node;
    }

    public Integer[] featureJsonToArr(JSONArray featuresJSON) {
        Integer[] features = new Integer[featuresJSON.size()];
        for (int i = 0; i < featuresJSON.size(); i++) {
            features[i] = ((Long) featuresJSON.get(i)).intValue();
        }
        featureList.addAll(Arrays.asList(features));
        return features;
    }

    public void setupRandomFeatures(int numberOfFeatures) {
        if (numberOfFeatures > featureList.size()) {
            return;
        }

        randomFeatureList.clear();

        while (randomFeatureList.size() < numberOfFeatures) {
            int randomIndex = random(0, featureList.size());
            int randomFeature = (Integer) featureList.toArray()[randomIndex];
            randomFeatureList.add(randomFeature);
        }
    }

    public Integer[] getRandomFeatArr() {
        Integer[] array = new Integer[randomFeatureList.size()];
        for (int i = 0; i < randomFeatureList.size(); i++) {
            array[i] = randomFeatureList.get(i);
        }
        return array;
    }

    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public Node[] getSocialNodes() {
        return socialNodes;
    }

    public Node[] getCentralNodes() {
        return centralNodes;
    }

    public Node[] getDecentralNodes() {
        return decentralNodes;
    }

    public RandEnvGenerator getRandEnvGen() {
        return randEnvGen;
    }

    public Set<Integer> getFeatures() {
        return usedFeatures;
    }

    public void sortByRelationshipDiversity() {
        for (int i = 1; i < socialNodes.length; i++) {
            SocialNode socialNode = (SocialNode) socialNodes[i];
            socialNode.resortEdges();
        }
    }

    public int getRandomNodeId() {
        return centralNodes[random(1, centralNodes.length - 1)].getId();
    }
}
