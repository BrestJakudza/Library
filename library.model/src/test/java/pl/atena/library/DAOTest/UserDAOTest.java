package pl.atena.library.DAOTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.fluttercode.datafactory.AddressDataValues;
import org.fluttercode.datafactory.ContentDataValues;
import org.fluttercode.datafactory.NameDataValues;
import org.fluttercode.datafactory.impl.DataFactory;
import org.fluttercode.datafactory.impl.DefaultAddressDataValues;
import org.fluttercode.datafactory.impl.DefaultContentDataValues;
import org.fluttercode.datafactory.impl.DefaultNameDataValues;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.atena.library.DAO.UserDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.model.Book;
import pl.atena.library.model.User;
import pl.atena.library.producers.LoggerProducer;

@RunWith(Arquillian.class)
public class UserDAOTest {

	@Inject
	private UserDAO userDAO;

	@Inject
	private DataGenerator dataGenerator;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(UserDAO.class)
				.addClass(DataGenerator.class)
				.addClass(User.class)
				.addClass(Book.class)
				.addClass(DataFactory.class)
				.addClass(NameDataValues.class)
				.addClass(AddressDataValues.class)
				.addClass(ContentDataValues.class)
				.addClass(DefaultNameDataValues.class)
				.addClass(DefaultAddressDataValues.class)
				.addClass(DefaultContentDataValues.class)
				.addClass(LoggerProducer.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
	}

	@Test
	public void testUpdate() {
		User user = dataGenerator.generateTestUser();
		userDAO.create(user);
		user.setName("newTestName");
		assertTrue(userDAO.update(user));
		assertEquals("newTestName", userDAO.findById(user.getId()).getName());
	}

	@Test
	public void testDelete() {
		User user = dataGenerator.generateTestUser();
		userDAO.create(user);
		assertNotNull(userDAO.findById(user.getId()));
		userDAO.delete(user.getId());
		assertTrue(userDAO.findById(user.getId()) == null);
	}

	@Test
	public void testCreate() {
		User user = dataGenerator.generateTestUser();
		userDAO.create(user);
		assertNotNull(user.getId());
	}

	@Test
	public void testFindById() {
		User user = dataGenerator.generateTestUser();
		userDAO.create(user);
		assertNotNull(userDAO.findById(user.getId()));
	}

	@Test
	public void testFingByName() {
		User user = dataGenerator.generateTestUser();
		userDAO.create(user);
		assertNotNull(userDAO.findByName(user.getName()));
		assertEquals(user.getName(), userDAO.findById(user.getId()).getName());
	}

	@Test
	public void testGetAllUsers() {
		List<User> users = dataGenerator.generateUsers(5);
		users.forEach(item -> userDAO.create(item));
		assertTrue(userDAO.readAllUsers().size() >= 5);
	}

}
