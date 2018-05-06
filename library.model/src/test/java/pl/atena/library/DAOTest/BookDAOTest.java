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
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.atena.library.DAO.BookDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.model.Book;
import pl.atena.library.model.User;

@RunWith(Arquillian.class)
public class BookDAOTest {

	@Inject
	private BookDAO bookDAO;
	
	@Inject
	private DataGenerator dataGenerator;
	
	@Test
	@InSequence(1)
	public void create() {
		Book book = dataGenerator.generateTestBook();
		bookDAO.create(book);
		assertNotNull(book);
		assertNotNull(book.getId());
	}
	
	@Test
	@InSequence(2)
	public void update() {
		Book book = dataGenerator.generateTestBook();
		bookDAO.create(book);
		book.setTitle("newTestBookTitle");
		assertTrue(bookDAO.update(book));
		assertEquals("newTestBookTitle", bookDAO.findById(book.getId()).getTitle());
	}

	@Test
	@InSequence(3)
	public void delete() {
		Book book = dataGenerator.generateTestBook();
		bookDAO.create(book);
		assertNotNull(bookDAO.findById(book.getId()));
		bookDAO.delete(book.getId());
		assertTrue(bookDAO.findById(book.getId()) == null);
	}

	@Test
	@InSequence(4)
	public void findAll() {
		List<Book> users = dataGenerator.generateBooks(5);
		users.forEach(item -> bookDAO.create(item));
		assertTrue(bookDAO.getAllBooks().size() >= 5);
	}

	@Test
	@InSequence(5)
	public void findById() {
		Book book = dataGenerator.generateTestBook();
		bookDAO.create(book);
		assertNotNull(bookDAO.findById(book.getId()));
	}

	@Deployment
    public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(BookDAO.class)
				.addClass(DataGenerator.class)
				.addClass(Book.class)
				.addClass(User.class)
				.addClass(DataFactory.class)
				.addClass(DataGenerator.class)
				.addClass(NameDataValues.class)
				.addClass(AddressDataValues.class)
				.addClass(ContentDataValues.class)
				.addClass(DefaultNameDataValues.class)
				.addClass(DefaultAddressDataValues.class)
				.addClass(DefaultContentDataValues.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
	            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
	}

}
