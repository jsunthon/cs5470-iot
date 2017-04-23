package logic;
import models.*;
import models.nodes.Node;
import models.nodes.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandEnvGenerator {
    private static final String MANUFACTURER_PREFIX = "manufacturer_";
    private static final String OWNER_PREFIX = "owner_";
    private final List<Manufacturer> manufacturers = new ArrayList<>();
    private final List<Owner> owners = new ArrayList<>();
    private final List<Node> nodes = new ArrayList<>();
    private static RandEnvGenerator randEnvGenerator;
    
    private RandEnvGenerator() {}
    
    public static RandEnvGenerator getInstance() {
    	if (randEnvGenerator == null) {
    		randEnvGenerator  = new RandEnvGenerator();
    	}
    	return randEnvGenerator;
    }

    public List<Manufacturer> genManufacturers(int manufCount,
                                 int manufFeatureCount) {
        while (manufCount > 0) {
            Manufacturer manufacturer = new Manufacturer(
                    MANUFACTURER_PREFIX + Manufacturer.getIdCounter());
            while (manufFeatureCount > 0) {
                int feature = genRandNumber(1, manufFeatureCount);
                manufacturer.addFeature(feature);
                manufFeatureCount--;
            }
            manufacturers.add(manufacturer);
            manufCount--;
        }
        return manufacturers;
    }

    public List<Owner> genOwners(int ownerCount) {
        while (ownerCount > 0) {
            Owner owner = new Owner(OWNER_PREFIX + Owner.getIdCounter());
            owners.add(owner);
            ownerCount--;
        }
        return owners;
    }

//    public Node genRandomizeNode(NodeType nodeType, int id, int nodeFeatureCount) {
//        if (owners.isEmpty() || manufacturers.isEmpty() || nodeType == null) return null;
//        int ownerIndex = genRandNumber(0, owners.size() - 1);
//        int manufIndex = genRandNumber(0, manufacturers.size() - 1);
//        int featureCount = genRandNumber(1, nodeFeatureCount);
//        Owner owner = owners.get(ownerIndex);
//        Manufacturer manufacturer = manufacturers.get(manufIndex);
//        Role role = Role.randomRole();
//        TimeToLive timeToLive = TimeToLive.randomTimeToLive();
//        Feature[] features = new Feature[featureCount];
//        for (int i = 0; i < features.length; i++) {
//            features[i] = Feature.randomFeature();
//        }
//        Node node = manufacturer.create(nodeType, id, role, timeToLive, features);
//        node.setOwner(owner);
//        node.setShare(genRandomShare());
//        nodes.add(node);
//        return node;
//    }
    
    /** 
     * Used to generate node with random attributes, such as manuf, owner, role, ttl, share, ect.
     * This will usually used to generate the social nodes first from the JSON file.
     * @param nodeType
     * @param id
     * @param nodeFeatures
     * @return
     */
    public Node genRandomizeNode(NodeType nodeType, int id, Integer... nodeFeatures) {
        if (owners.isEmpty() || manufacturers.isEmpty() || nodeType == null) return null;
        int ownerIndex = genRandNumber(0, owners.size() - 1);
        int manufIndex = genRandNumber(0, manufacturers.size() - 1);
        Owner owner = owners.get(ownerIndex);
        Manufacturer manufacturer = manufacturers.get(manufIndex);
        Role role = Role.randomRole();
        TimeToLive timeToLive = TimeToLive.randomTimeToLive();
        Node node = manufacturer.create(nodeType, id, role, timeToLive, nodeFeatures);
        node.setOwner(owner);
        node.setShare(genRandomShare());
        nodes.add(node);
        return node;
    }
    
    /**
     * Used to generate a node given non-random attributes
     * @param nodeType
     * @param id
     * @param owner
     * @param manufacturer
     * @param role
     * @param timeToLive
     * @param nodeFeatures
     * @param share
     * @return
     */
    public Node genNode(NodeType nodeType,
    					int id,
    					Owner owner,
    					Manufacturer manufacturer,
    					Role role,
    					TimeToLive timeToLive,
    					Integer[] features,
    					boolean share) {
    	Node node = manufacturer.create(nodeType, id, role, timeToLive, features);
    	node.setOwner(owner);
    	node.setShare(share);
    	nodes.add(node);
    	return node;
    }
    					
    private boolean genRandomShare() {
        return genRandNumber(0, 1) == 1;
    }

    public static int genRandNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public List<Node> getNodes() {
        return nodes;
    }
    
    public void reset() {
    	manufacturers.clear();
    	owners.clear();
    	nodes.clear();
    }
}
