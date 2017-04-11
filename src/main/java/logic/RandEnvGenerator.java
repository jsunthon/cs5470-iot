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
            int featureCount = genRandNumber(1, manufFeatureCount);
            while (featureCount > 0) {
                Feature feature = Feature.randomFeature();
                manufacturer.addFeature(feature);
                featureCount--;
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

    public Node genRandomizeNode(NodeType nodeType, int id, int nodeFeatureCount) {
        if (owners.isEmpty() || manufacturers.isEmpty() || nodeType == null) return null;
        int ownerIndex = genRandNumber(0, owners.size() - 1);
        int manufIndex = genRandNumber(0, manufacturers.size() - 1);
        int featureCount = genRandNumber(1, nodeFeatureCount);
        Owner owner = owners.get(ownerIndex);
        Manufacturer manufacturer = manufacturers.get(manufIndex);
        Role role = Role.randomRole();
        TimeToLive timeToLive = TimeToLive.randomTimeToLive();
        Feature[] features = new Feature[featureCount];
        for (int i = 0; i < features.length; i++) {
            features[i] = Feature.randomFeature();
        }
        Node node = manufacturer.create(nodeType, id, role, timeToLive, features);
        node.setOwner(owner);
        node.setShare(genRandomShare());
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
