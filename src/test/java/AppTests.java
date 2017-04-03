import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Node;

public class AppTests {
	Logger logger = LoggerFactory.getLogger(AppTests.class);
	@Test
	public void testCreateOrGetNode() {
		App app = new App();
		app.init();
		Node node1 = app.createOrGetNode(1);
		Assert.assertNotNull(node1);
		Node node2 = app.createOrGetNode(2);
		Assert.assertNotNull(node2);
	}
	
	@Test
	public void testFormRelationship() {
		App app = new App();
		app.init();
		Node node1 = app.createOrGetNode(1);
		List<Long> neighbors = new ArrayList<>();
		neighbors.add(new Long(2));
		neighbors.add(new Long(4));
		neighbors.add(new Long(6));
		app.formRelationship(node1, neighbors.iterator());
		Assert.assertEquals(3, (int) node1.getCentrality());
		neighbors.add(new Long(5));
		Assert.assertEquals(4,  neighbors.size());
		app.formRelationship(node1, neighbors.iterator());
		Assert.assertEquals(4, (int) node1.getCentrality());
		Node node2 = app.createOrGetNode(4);
		List<Long> neighbors2 = new ArrayList<>();
		neighbors2.add(new Long(5));
		neighbors2.add(new Long(10));
		app.formRelationship(node2, neighbors2.iterator());
		Assert.assertEquals(2, (int) node2.getCentrality()); 
	}
}
