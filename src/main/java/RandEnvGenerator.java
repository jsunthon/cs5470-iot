import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandEnvGenerator {
    private static final String MANUFACTURER_PREFIX = "manufacturer_";
    private static final String OWNER_PREFIX = "owner_";
    private List<Manufacturer> manufacturers;
    private List<Owner> owners;
    private List<Node> nodes;

    public RandEnvGenerator() {
        manufacturers = new ArrayList<>();
        owners = new ArrayList<>();
        nodes = new ArrayList<>();
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

    public Node genRandomizeNode(int id, int nodeFeatureCount) {
        if (owners.isEmpty() || manufacturers.isEmpty()) return null;
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
        Node node = manufacturer.create(id, role, timeToLive, features);
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
}